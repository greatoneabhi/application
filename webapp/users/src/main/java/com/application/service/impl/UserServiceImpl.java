package com.application.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.application.common.CommonConstants;
import com.application.entity.UserEntity;
import com.application.entity.UserKey;
import com.application.logger.Loggable;
import com.application.model.User;
import com.application.repository.UserRepository;
import com.application.service.RestService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserServiceImpl implements RestService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Loggable
	public UserEntity create(final User user) {

		List<UserEntity> dbUser = userRepository.findByPrimaryKeyUsername(user.getUsername());
		if (Objects.nonNull(dbUser) && !dbUser.isEmpty()) {
			throw new DataIntegrityViolationException(CommonConstants.RESOURCE_ALREADY_EXISTS);
		}
		return saveUser(user);
	}

	public UserEntity get(String username) {
		List<UserEntity> dbUser = userRepository.findByPrimaryKeyUsername(username);
		if (Objects.isNull(dbUser) || dbUser.isEmpty()) {
			throw new ResourceNotFoundException(CommonConstants.RESOURCE_NOT_FOUND);
		}
		return dbUser.get(0);
	}

	public UserEntity update(String username, User user) {
		List<UserEntity> dbUser = userRepository.findByPrimaryKeyUsername(username);
		if (Objects.isNull(dbUser) || dbUser.isEmpty()) {
			throw new ResourceNotFoundException(CommonConstants.RESOURCE_NOT_FOUND);
		}
		return saveUser(user);
	}

	public List<UserEntity> getAll() {
		Iterable<UserEntity> userIterable = userRepository.findAll();
		final List<UserEntity> usersList = new ArrayList<UserEntity>();
		userIterable.forEach(user -> usersList.add(user));
		return usersList;
	}
	
	private UserEntity saveUser(User user) {
		UserEntity userEntity = objectMapper.convertValue(user, UserEntity.class);
		UserKey userPrimaryKey = objectMapper.convertValue(user, UserKey.class);
		userEntity.setPrimaryKey(userPrimaryKey);
		return userRepository.save(userEntity);
	}
}
