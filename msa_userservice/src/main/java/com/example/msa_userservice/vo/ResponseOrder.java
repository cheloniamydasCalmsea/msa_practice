package com.example.msa_userservice.vo;

import lombok.Data;

@Data
public class ResponseOrder {
	private String produckId;
	private Integer qty;
	private Integer unitPrice;
	private Integer totalPrice;
	private Data createAt;
	
	private String orderId;
	
}
