package com.sample.spring.repository;


import com.sample.spring.entity.RefreshTokenEntity;

public interface RefreshTokenRepository {

    RefreshTokenEntity findByTokenId(String tokenId);

    void create(RefreshTokenEntity entity);

    void delete(RefreshTokenEntity entity);
}