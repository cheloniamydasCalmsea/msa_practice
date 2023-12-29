package com.example.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config>{
	public CustomFilter(){
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		// TODO Auto-generated method stub
		// Custom Pre Filter
		
		return (exchange, chain) ->{
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();
			
			
			log.info("Custom PRE filter : request id -> {}", request.getId());
			
			//Custom Post Filter
			return chain.filter(exchange).then(Mono.fromRunnable(()->{//비동기 객체 webflux에서 추가된 사항 Mono
				log.info("Custom POST filter : response id -> {}", response.getStatusCode());
			}));
		};
	}

	public static class Config{
		//Put the configuration properties
	}
}
