package com.stock.dto;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class OrderDTO {

	private Long ordId;

	private String ordStatus;

	private String createdBy;

	@Setter(AccessLevel.PRIVATE)
	private Date createdOn;

	private String custName;

	private String custMobile;
	
	private List<ItemDTO> item;
}
