package com.example.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {

//	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/first-service/**")
						.filters(f->f.addRequestHeader("first-request", "firest-request-header")
										.addResponseHeader("first-response", "first-response-header"))
						.uri("http://localhost:8081/"))		
				.route(r -> r.path("/second-service/**")
						.filters(f->f.addRequestHeader("second-request", "second-request-header")
										.addResponseHeader("second-response", "second-response-header"))
						.uri("http://localhost:8082/"))
				.build();//#memory에 반영
				//#final > 사용자로부터 first-service요청이들어오면 uri로 보내부는데 그사이에 filter를 적용하여 requestHeader와 responseHeader를 셋팅해줍니다.
	}
}
