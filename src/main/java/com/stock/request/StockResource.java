package com.stock.request;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Component
@Getter
@Setter
public class StockResource {

    private Long id;

    private String name;

    private Long quantity;

    private Double sellPrice;

    private Double buyPrice;

    private String description;

    private Date purchaseDate;

    private Date expiryDate;

    private String code;

    private String brand;

    private Float costPerPiece;

    private Float totalCost;
}
