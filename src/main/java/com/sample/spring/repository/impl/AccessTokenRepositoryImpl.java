package com.sample.spring.repository.impl;

import com.sample.spring.common.model.IdentityEntity;
import com.sample.spring.entity.AccessTokenEntity;
import com.sample.spring.mapper.AccessTokenMapper;
import com.sample.spring.repository.AccessTokenRepository;
import com.sample.spring.repository.BasicMongoRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AccessTokenRepositoryImpl extends BasicMongoRepository<AccessTokenEntity> implements AccessTokenRepository {
    private final MongoTemplate template;

    protected AccessTokenRepositoryImpl(MongoTemplate template, Class<AccessTokenEntity> clazz) {
        super(template, clazz);
        this.template = template;
    }

    @Override
    public AccessTokenEntity findByTokenId(String tokenId) {
        if (ObjectUtils.isEmpty(tokenId))
            return null;
        return this.findByField(AccessTokenEntity.Fields.tokenId, tokenId);
    }

    @Override
    public void copyObjectToObject(AccessTokenEntity entityA, AccessTokenEntity entityB) {
        AccessTokenMapper.INSTANCE.entityToEntity(entityA, entityB);
    }

    @Override
    public AccessTokenEntity findByRefreshToken(String refreshToken) {
        if (ObjectUtils.isEmpty(refreshToken))
            return null;
        return this.findByField(AccessTokenEntity.Fields.refreshToken, refreshToken);
    }

    @Override
    public AccessTokenEntity findByAuthenticationId(String authenticationId) {
        if (ObjectUtils.isEmpty(authenticationId))
            return null;
        return this.findByField(AccessTokenEntity.Fields.authenticationId, authenticationId);
    }

    @Override
    public List<AccessTokenEntity> findByClientId(String clientId) {
        if (ObjectUtils.isEmpty(clientId))
            return null;
        return this.findMany(AccessTokenEntity.Fields.authenticationId, clientId);
    }

    @Override
    public List<AccessTokenEntity> findByClientIdAndUsername(String clientId, String username) {
        if (ObjectUtils.anyNull(clientId, username))
            return null;
        return this.findMany(Map.of(AccessTokenEntity.Fields.authenticationId, clientId, AccessTokenEntity.Fields.username, username));
    }

    @Override
    public void delete(AccessTokenEntity entity) {
        if (ObjectUtils.isEmpty(entity.getId()))
            return;
        this.removeByField(IdentityEntity.Fields.id, entity.getId());
    }
}