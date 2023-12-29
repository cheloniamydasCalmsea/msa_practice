package com.example.msa_userservice.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class Greeting {
	
	@Value("${greeting.message}")
	private String message;

}
