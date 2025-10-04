package com.stock.service;

import com.stock.bean.Order;
import com.stock.bean.Stock;
import com.stock.dto.ItemDTO;
import com.stock.dto.OrderDTO;
import com.stock.exception.NoStockFound;
import com.stock.mapper.StockMapper;
import com.stock.repository.StockRepository;
import com.stock.request.StockResource;
import com.stock.response.StockResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@CacheConfig(cacheNames = "stockDetails")
public class StockService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final StockMapper stockMapper;

    private final StockRepository stockRepository;

    private final KafkaTemplate<String, Order> kafkaTemplate;

    private final RedisCacheManager cacheManager;

    public ResponseEntity<String> createStock(Stock stock) throws Exception {
        Stock stockSaved = stockRepository.save(stock);
        refreshAllStocksCache();
        evictFilteredCaches();
        return createResponseJSON(stockSaved, HttpStatus.CREATED);
    }

    private void evictCache(String brand, String name) {
        String brandKey = "stockDetails::brand:" + (brand != null ? brand : "ALL") + ":*";
        String nameKey = "stockDetails::brand:*:name:" + (name != null ? name : "ALL");

        log.info("Evicting cache for brand={} and name={}", brand, name);

        // Use Redis SCAN instead of KEYS in production for better performance
        deleteKeysByPattern(brandKey);
        deleteKeysByPattern(nameKey);
    }

    private void deleteKeysByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Deleted {} cache keys", keys.size());
        }
    }

    @KafkaListener(topics = {"order-created"}, groupId = "order-group")
    public void updateStockIfOrderCompleted(@Payload ConsumerRecord<String, Object> consumerRecord,
                                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println("=====Inside Stock updateStockIfOrderCompleted====================" + consumerRecord);
        Object message = consumerRecord.value();
        System.out.println("=====message=============" + message);
        System.out.println("Received message of type: " + message.getClass().getName());

        if (message instanceof OrderDTO) {
            System.out.println("Message Type is Order");
            OrderDTO orderDTO = (OrderDTO) message;
            List<ItemDTO> lisItemDTOs = orderDTO.getItem();
            if (StringUtils.equals(orderDTO.getOrdStatus(), "CONFIRMED")) {
                System.out.println("order status is confirmed");
                // order.getItems().parallelStream().forEach(item ->
                // updateStockOnOrderCreation(item));
                lisItemDTOs.stream().map(item -> {
                    updateStockOnOrderCreation(item);
                    return item; // Return the item (or something else if needed)
                }).collect(Collectors.toList());
            }
        }
    }

    private Stock updateStockOnOrderCreation(ItemDTO item) {
        Stock stock = stockRepository.findByName(item.getName());
        stock.setQuantity(stock.getQuantity() - item.getQuantity());
        return stockRepository.save(stock);
    }

    private ResponseEntity<String> createResponseJSON(Stock stock, HttpStatus status) throws Exception {
        ResponseEntity<String> response = null;

        try {
            response = ResponseEntity.created(new URI(String.valueOf(stock.getId()))).build();
        } catch (Exception ex) {
        }

        return response;
    }

    @Cacheable(
            key = "'brand:' + (#stockResource.brand != null && #stockResource.brand.trim().length() > 0 ? #stockResource.brand : 'ALL') + " +
                    "':name:' + (#stockResource.name != null && #stockResource.name.trim().length() > 0 ? #stockResource.name : 'ALL')"
    )
    public List<StockResponse> getStockDetails(StockResource stockResource) {

        log.info("👉 Fetching from DB for brand={}, name={}", stockResource.getBrand(), stockResource.getName());

        String searchByField = StringUtils.isNotBlank(stockResource.getBrand())
                ? stockResource.getBrand()
                : stockResource.getName();

        List<Stock> listOfStock = stockRepository.getStockDetails(stockResource);

        if (listOfStock == null || listOfStock.isEmpty()) {
            throw new NoStockFound("No stock found for " + searchByField);
        }

        return listOfStock.stream()
                .map(stockMapper::toResponse)
                .toList();
    }

    private void refreshAllStocksCache() {
        List<Stock> allStocks = stockRepository.findAll();
        List<StockResponse> responseList = allStocks.stream()
                .map(stockMapper::toResponse)
                .toList();

        Cache cache = cacheManager.getCache("stockDetails");
        if (cache != null) {
            cache.put("brand:ALL:name:ALL", responseList);
            log.info("✅ Refreshed ALL stocks cache, size={}", responseList.size());
        }
    }

    /** Clear filtered caches (leave ALL intact) */
    private void evictFilteredCaches() {
        Cache cache = cacheManager.getCache("stockDetails");
        if (cache != null) {
            // ⚠️ RedisCacheManager doesn’t support wildcard eviction natively.
            // You’d need a RedisTemplate to scan keys, or you evict known keys if tracked.
            // For simplicity here, we clear everything except ALL.
            cache.clear();
            log.info("🗑️ Cleared filtered caches.");
            refreshAllStocksCache(); // put back ALL after clear
        }
    }

}
