package com.application.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.application.entity.UserEntity;
import com.application.logger.Loggable;
import com.application.model.User;
import com.application.service.impl.UserServiceImpl;

@CrossOrigin
@RestController
@RequestMapping(value = "/applications/api/v1")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class UserController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	@Loggable
	public String createUser(@Valid @RequestBody final User user) {
		UserEntity userEntity = userServiceImpl.create(user);
		return userEntity.getPrimaryKey().getUsername();
	}

	@RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@Loggable
	public UserEntity getUser(@PathVariable String username) {
		return userServiceImpl.get(username);
	}

	@RequestMapping(value = "/users/{username}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	@Loggable
	public UserEntity replaceUser(@PathVariable String username, @Valid @RequestBody User user) {
		return userServiceImpl.update(username, user);
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@Loggable
	public List<UserEntity> getAllUsers() {
		return userServiceImpl.getAll();
	}
}
