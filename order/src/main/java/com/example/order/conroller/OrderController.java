package com.example.order.conroller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order.dto.OrderDto;
import com.example.order.jpa.OrderEntity;
import com.example.order.service.OrderService;
import com.example.order.vo.RequestOrder;
import com.example.order.vo.ResponseOrder;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order-service")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	
	@GetMapping("/health_check")
	public String status(HttpServletRequest request) {
		return String.format("It's Working in Order Service on Port %s", request.getServerPort());
	}
	
	@PostMapping("/{userId}/orders")
	public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails){
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		OrderDto orderDto = modelMapper.map(orderDetails, OrderDto.class);
		orderDto.setUserId(userId);
		OrderDto createDto = orderService.createOrder(orderDto);
		
		ResponseOrder returnValue = modelMapper.map(createDto, ResponseOrder.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}
	
	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId){
		
		Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);
		
		List<ResponseOrder> result = new ArrayList<>();

		orderList.forEach(v->
			result.add(new ModelMapper().map(v, ResponseOrder.class))
				);

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	

}
