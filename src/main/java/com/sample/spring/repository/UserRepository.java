package com.sample.spring.repository;


import com.sample.spring.entity.UserEntity;

public interface UserRepository {

    UserEntity findByUsername(String username);

    UserEntity findOneById(String id);

    void save(UserEntity entity);
}
