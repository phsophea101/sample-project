package com.sample.spring.repository.impl;

import com.sample.spring.common.model.IdentityEntity;
import com.sample.spring.common.model.ModelQueryDto;
import com.sample.spring.common.model.QueryCondition;
import com.sample.spring.entity.AccessTokenEntity;
import com.sample.spring.repository.AccessTokenRepository;
import com.sample.spring.repository.BasicMongoRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class AccessTokenRepositoryImpl extends BasicMongoRepository<AccessTokenEntity> implements AccessTokenRepository {
    @Autowired
    protected AccessTokenRepositoryImpl(MongoTemplate template) {
        super(template);
    }

    @Override
    public AccessTokenEntity findByTokenId(String tokenId) {
        if (ObjectUtils.isEmpty(tokenId))
            return null;
        return this.findByField(new ModelQueryDto(AccessTokenEntity.Fields.tokenId, QueryCondition.IS, tokenId));
    }

    @Override
    public AccessTokenEntity findByRefreshToken(String refreshToken) {
        if (ObjectUtils.isEmpty(refreshToken))
            return null;
        return this.findByField(new ModelQueryDto(AccessTokenEntity.Fields.refreshToken, QueryCondition.IS, refreshToken));
    }

    @Override
    public AccessTokenEntity findByAuthenticationId(String authenticationId) {
        if (ObjectUtils.isEmpty(authenticationId))
            return null;
        return this.findByField(new ModelQueryDto(AccessTokenEntity.Fields.authenticationId, QueryCondition.IS, authenticationId));
    }

    @Override
    public List<AccessTokenEntity> findByClientId(String clientId) {
        if (ObjectUtils.isEmpty(clientId))
            return Collections.emptyList();
        return this.findMany(new ModelQueryDto(AccessTokenEntity.Fields.authenticationId, QueryCondition.IS, clientId));
    }

    @Override
    public List<AccessTokenEntity> findByClientIdAndUsername(String clientId, String username) {
        if (ObjectUtils.anyNull(clientId, username))
            return Collections.emptyList();
        List<ModelQueryDto> modelQueries = new ArrayList<>();
        modelQueries.add(new ModelQueryDto(AccessTokenEntity.Fields.authenticationId, QueryCondition.IS, clientId));
        modelQueries.add(new ModelQueryDto(AccessTokenEntity.Fields.username, QueryCondition.IS, username));
        return this.findMany(modelQueries);
    }

    @Override
    public void delete(AccessTokenEntity entity) {
        if (ObjectUtils.isEmpty(entity.getId()))
            return;
        this.removeByField(new ModelQueryDto(IdentityEntity.Fields.id, QueryCondition.IS, entity.getId()));
    }
}