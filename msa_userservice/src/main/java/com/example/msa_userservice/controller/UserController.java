package com.example.msa_userservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.msa_userservice.dto.UserDto;
import com.example.msa_userservice.jpa.UserEntity;
import com.example.msa_userservice.service.UserService;
import com.example.msa_userservice.vo.Greeting;
import com.example.msa_userservice.vo.RequestUser;
import com.example.msa_userservice.vo.ResponseUser;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class UserController {
	
	Environment env;
	//@Value("${greeting.message}")
	
	@Autowired
	private Greeting greeing;
	
	private UserService userService;
	
	@Autowired
	public UserController(Environment env, UserService userService) {
		this.env = env;
		this.userService = userService;
	}

	@GetMapping("/health_check")
	public String status(HttpServletRequest request) {
		return String.format("It's Working in User Service on Port %s", request.getServerPort());
	}

	@GetMapping("/welcome")
	public String welcome() {
//		return env.getProperty("greeting.message");
		
		return greeing.getMessage();
	}
	
	
	@PostMapping("/users")
	public ResponseEntity createUser(@RequestBody RequestUser user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = mapper.map(user, UserDto.class);

		userService.createUser(userDto);
		
		ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
		
//		return new ResponseEntity(HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<ResponseUser>> getUsers(){
		
		Iterable<UserEntity> userList = userService.getUserByAll();
		
		List<ResponseUser> result = new ArrayList<>();
		
		userList.forEach(v->{
			result.add(new ModelMapper().map(v, ResponseUser.class));
		});
		
		
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
		
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity getUser(@PathVariable("userId") String userId){
		UserDto userDto = userService.getUserByUserId(userId);
		ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);
		
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
		
	}

}
