package com.example.msa_userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.msa_userservice.dto.UserDto;
import com.example.msa_userservice.jpa.UserEntity;

public interface UserService extends UserDetailsService{
	UserDto createUser(UserDto userDto);

	UserDto getUserByUserId(String userId);

	Iterable<UserEntity> getUserByAll();
}
