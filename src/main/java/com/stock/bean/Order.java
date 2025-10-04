package com.stock.bean;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
	@Setter(AccessLevel.NONE)
	private Long ordId;
	
	private List<Items> items;
	
	private String ordStatus;
}
