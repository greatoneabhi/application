package com.application.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {

}
