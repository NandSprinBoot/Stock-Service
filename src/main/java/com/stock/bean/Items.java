package com.stock.bean;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Items {
	@Setter(AccessLevel.NONE)
    private Long prd_Id;
	
	private String name;
	
	private Long quantity;
	
	private Double sellPrice;
	
	private String description;
	
	private Date sellDate;
	
	private Date expiryDate;

}
