package com.stock.service;

import com.stock.bean.Stock;
import com.stock.dto.PageRequestDto;
import com.stock.request.StockResource;
import com.stock.response.PageResponse;
import com.stock.response.StockResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StockService {
    public ResponseEntity<String> createStock(Stock stock) throws Exception;

    public List<StockResponse> getStockDetails(StockResource stockResource);

    public PageResponse<StockResponse> getStocksForOrderCreation(String stockName, PageRequestDto pageRequestDto);

    public void deleteStocks(String stockName);
}
