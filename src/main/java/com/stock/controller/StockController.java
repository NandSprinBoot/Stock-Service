package com.stock.controller;

import com.stock.bean.Stock;
import com.stock.dto.PageRequestDto;
import com.stock.request.StockResource;
import com.stock.response.PageResponse;
import com.stock.response.StockResponse;
import com.stock.service.impl.StockServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockServiceImpl stockService;

    @PostMapping("/create")
    public ResponseEntity<String> createStock(@RequestBody Stock stock) {
        ResponseEntity<String> response = null;
        try {
            response = stockService.createStock(stock);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockResponse>> getStockDetails(@RequestBody StockResource stockResource) {
        List<StockResponse> stockResponses = stockService.getStockDetails(stockResource);
        return ResponseEntity.ok(stockResponses);
    }

    @GetMapping(value = "/search/{stockName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<StockResponse>> getStocksForOrderCreation(
            @PathVariable String stockName,
            @ModelAttribute PageRequestDto pageRequestDto) {

        log.info("Fetching stocks for search: {} with pagination {}", stockName, pageRequestDto);

        PageResponse<StockResponse> response = stockService.getStocksForOrderCreation(stockName, pageRequestDto);
        return ResponseEntity.ok(response);
    }

    /*@PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteStock(@PathVariable String name) {
        stockService.deleteStocks(name);
        return ResponseEntity.ok("Stock deleted successfully for : " + name);
    }*/

    /*@GetMapping("/test")
    public ResponseEntity<List<StockResponse>>  test() {
        List<StockResponse> list = new ArrayList<>();
        StockResponse res = new StockResponse();
        res.setId(1L);
        res.setName("TestItem");
        res.setQuantity(10L);
        res.setSellPrice(100.0);
        list.add(res);
        return ResponseEntity.ok(list);
    }*/

}
