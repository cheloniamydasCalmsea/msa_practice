package com.example.catalog.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catalog.dto.CatalogDto;
import com.example.catalog.jpa.CatalogEntity;
import com.example.catalog.service.CatalogService;
import com.example.catalog.vo.ResponseCatalog;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
	
	//실행 X
//	private final Environment env = null;
//	private final CatalogService catalogService = null;
	
	//실행 O
//	@Autowired
//	CatalogService catalogService;
//	Environment env;
	private CatalogService catalogService;

	@Autowired
	public CatalogController(Environment env,CatalogService catalogService){
//		this.env = env;
		this.catalogService = catalogService;
	}
	
	
	@GetMapping("/health_check")
	public String status(HttpServletRequest request) {
		return String.format("It's Working in Catalog Service on Port %s", request.getServerPort());
	}

	
	@GetMapping("/catalogs")
	public ResponseEntity<List<ResponseCatalog>> catalogs(){
		
		Iterable<CatalogEntity> catalogList = catalogService.getAllCatalogs();
		
		List<ResponseCatalog> result = new ArrayList<>();
		
		catalogList.forEach(v->{
			result.add(new ModelMapper().map(v, ResponseCatalog.class));
		});
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
		
	}
}
