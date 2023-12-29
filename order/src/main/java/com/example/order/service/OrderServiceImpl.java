package com.example.order.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.order.dto.OrderDto;
import com.example.order.jpa.OrderEntity;
import com.example.order.jpa.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	OrderRepository repository;

	@Override
	public OrderDto createOrder(OrderDto orderDetails) {
		orderDetails.setOrderId(UUID.randomUUID().toString());
		orderDetails.setTotalPrice(orderDetails.getQty()*orderDetails.getUnitPrice());
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		OrderEntity orderEntity = modelMapper.map(orderDetails, OrderEntity.class);
		repository.save(orderEntity);
		
		OrderDto returnValue = modelMapper.map(orderEntity, OrderDto.class);
		return returnValue;
		
	}

	@Override
	public OrderDto getOrderByOrderId(String orderId) {
		OrderEntity orderEntity = repository.findByOrderId(orderId);
		
		OrderDto orderDto = new ModelMapper().map(orderEntity, OrderDto.class);
		
		return orderDto;
	}

	@Override
	public Iterable<OrderEntity> getOrdersByUserId(String userId) {
		return repository.findByUserId(userId);
	}

}
