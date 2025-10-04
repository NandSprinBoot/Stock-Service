package com.stock.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class StockResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;

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
