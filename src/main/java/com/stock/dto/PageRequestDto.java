package com.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequestDto {
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String direction = "asc";
}
