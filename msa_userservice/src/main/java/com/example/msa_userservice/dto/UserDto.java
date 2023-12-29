package com.example.msa_userservice.dto;

import java.util.Date;
import java.util.List;

import com.example.msa_userservice.vo.ResponseOrder;

import lombok.Data;

@Data
public class UserDto {

	private String email;
	private String name;
	private String pwd;
	private String userId;
	private Date createAt;
	
	private String encrytedPwd;
	private List<ResponseOrder> orders;
}
