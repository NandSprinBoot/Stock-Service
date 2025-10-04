package com.stock.mapper;


import com.stock.bean.Stock;
import com.stock.response.StockResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {

public StockResponse toResponse(Stock stock);
}
