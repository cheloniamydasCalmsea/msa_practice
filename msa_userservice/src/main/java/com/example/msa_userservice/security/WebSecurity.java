package com.example.msa_userservice.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.msa_userservice.service.UserService;
import com.example.msa_userservice.util.Utils;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.security.Security;
import java.util.function.Supplier;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    private final Environment env;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectPostProcessor objectPostProcessor;
    private final AuthenticationConfiguration authenticationConfiguration;

    public static final String ALLOWED_IP_ADDRESS = "192.168.0.61";
    public static final String SUBNET = "/32";
    public static final IpAddressMatcher ALLOWED_IP_ADDRESS_MATCHER = new IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET);
    
    private static final String[] WHITE_LIST = {
            "/users/**",
            "/",
            "/**"
    };
    
    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
    	IpAddressMatcher hasIpAddress = new IpAddressMatcher("192.168.0.0");
    	
    	http.csrf(csrf -> csrf.disable());
    	http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

    	http.httpBasic();
    	http.authorizeHttpRequests(authorize -> 
							{
								try {
									authorize
										.requestMatchers("/users/**").permitAll()
										.requestMatchers("/**").access(this::hasIpAddress)
										.and().addFilter(getAuthenticationFilter())
										;
								} catch (Exception e) {
									System.out.println("exception : "+ e.getMessage());
									e.printStackTrace();
								}
							}
    			);

    	//인증처리를 위한로직 : db_pwd(encrypted) == input_pwd (encrypted)
    	AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
    	authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

        return http.build();
    	 
    }

	private UsernamePasswordAuthenticationFilter getAuthenticationFilter() throws Exception {
		System.out.println(":: use getAuthenticationFilter ::");
		UsernamePasswordAuthenticationFilter authenticationFilter = new UsernamePasswordAuthenticationFilter();
		authenticationFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());

		//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authenticationFilter;
	}

	private AuthorizationDecision hasIpAddress(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
    	System.out.println("use hasIpAddress method");
    	System.out.println(authentication);
    	System.out.println(authentication.get());

        return new AuthorizationDecision(ALLOWED_IP_ADDRESS_MATCHER.matches(Utils.getClientIP(object.getRequest())));
    }

  //ex) 기존의 경우 AuthenticationManagerBuilder 를 오버라이드 하여 사용 하였지만 filterChain 안에서 호출 하여 설정 합니다.
  	/* 
  	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
  		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
  	}
  	*/

//  	private AuthenticationFilter getAuthenticationFilter() throws Exception {
//  		System.out.println("use getAuthenticationFilter Filter");
////  		AuthenticationManager authenticationManager = new Auth;
//  		AuthenticationFilter authenticationFilter = new AuthenticationFilter();
//  		authenticationFilter.setAuthenticationManager(authenticationManager());
//  		return authenticationFilter;
//  	}

  	
}