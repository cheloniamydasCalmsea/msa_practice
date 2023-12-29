package com.example.msa_userservice.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.msa_userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			System.out.println(request);
			System.out.println("======================");
			System.out.println(request.getInputStream());
			System.out.println(request.getInputStream().toString());
			System.out.println("======================#2");
			System.out.println("======================#3");
			RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
			
			return getAuthenticationManager().authenticate(
						new UsernamePasswordAuthenticationToken(
								creds.getEmail()
								, creds.getPassword()
								, new ArrayList<>()
						)
			);
			
		}catch(Exception e){
			throw new RuntimeException();
			
		}
		
//		return super.attemptAuthentication(request, response);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
	
	

}
