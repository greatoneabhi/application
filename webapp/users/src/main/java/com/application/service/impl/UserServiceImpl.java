package com.application.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.application.common.CommonConstants;
import com.application.entity.UserEntity;
import com.application.service.RestService;

@Service
public class UserServiceImpl implements RestService {

    @Autowired
    UserRepository userRepository;

    public UserEntity create(final UserEntity user) {
        if(userRepository.exists(user.getEmailId())) {
            throw new DataIntegrityViolationException(CommonConstants
                    .RESOURCE_ALREADY_EXISTS);
        }
        return userRepository.save(user);
    }

    public UserEntity get(String email) {
        UserEntity user = userRepository.findOne(email);
        if( null == user ) {
            throw new ResourceNotFoundException(CommonConstants
                    .RESOURCE_NOT_FOUND);
        }
        return user;
    }

    public UserEntity update(String guid, UserEntity user) {
        UserEntity dbuser = userRepository.findOne(guid);
        if( null == dbuser ) {
            throw new ResourceNotFoundException(CommonConstants
                    .RESOURCE_NOT_FOUND);
        }
        userRepository.save(user);
        return user;
    }

    public List<UserEntity> getAll() {
        Iterable<UserEntity> userIterable = userRepository.findAll();
        final List<UserEntity> usersList = new ArrayList<UserEntity>();
        userIterable.forEach(user -> usersList.add(user));
        return usersList;
    }

}

interface UserRepository extends CrudRepository<UserEntity, String> {

}
