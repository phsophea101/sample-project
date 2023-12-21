package com.sample.spring.repository;

import com.sample.spring.entity.AccessTokenEntity;

import java.util.List;

public interface AccessTokenRepository {

    AccessTokenEntity findByTokenId(String tokenId);

    void create(AccessTokenEntity entity);

    AccessTokenEntity findByRefreshToken(String refreshToken);

    AccessTokenEntity findByAuthenticationId(String authenticationId);

    List<AccessTokenEntity> findByClientId(String clientId);

    List<AccessTokenEntity> findByClientIdAndUsername(String clientId, String userName);

    void delete(AccessTokenEntity entity);
}