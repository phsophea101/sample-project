package com.sample.spring.repository;


import com.sample.spring.entity.RefreshTokenEntity;

public interface RefreshTokenRepository {

    RefreshTokenEntity findByTokenId(String tokenId);

    void save(RefreshTokenEntity entity);

    void delete(RefreshTokenEntity entity);
}