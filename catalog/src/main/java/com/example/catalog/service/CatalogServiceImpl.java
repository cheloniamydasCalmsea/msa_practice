package com.example.catalog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.catalog.jpa.CatalogEntity;
import com.example.catalog.jpa.CatalogRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService{
	
	
	private final CatalogRepository caRepository;

	@Override
	public Iterable<CatalogEntity> getAllCatalogs() {
		return caRepository.findAll();
	}

}
