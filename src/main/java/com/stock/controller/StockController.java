package com.stock.controller;

import com.stock.bean.Stock;
import com.stock.request.StockResource;
import com.stock.response.StockResponse;
import com.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

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

    @PostMapping(value = "/filter/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockResponse>> getStockDetails(@RequestBody StockResource stockResource) {
        List<StockResponse> stockResponses = stockService.getStockDetails(stockResource);
        return ResponseEntity.ok(stockResponses);
    }

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
