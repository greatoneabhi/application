package com.application.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.UserEntity;
import com.application.entity.UserKey;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UserKey> {
	
	List<UserEntity> findByPrimaryKeyUsername(String username);
}
