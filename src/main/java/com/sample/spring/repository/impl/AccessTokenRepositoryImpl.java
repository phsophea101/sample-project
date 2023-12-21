package com.sample.spring.repository.impl;

import com.sample.spring.entity.AccessTokenEntity;
import com.sample.spring.repository.AccessTokenRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class AccessTokenRepositoryImpl implements AccessTokenRepository {
    private final MongoTemplate template;

    @Override
    public AccessTokenEntity findByTokenId(String tokenId) {
        if (ObjectUtils.isEmpty(tokenId))
            return null;
        Query query = Query.query(Criteria.where(AccessTokenEntity.Fields.tokenId).is(tokenId));
        return this.template.findOne(query, AccessTokenEntity.class);
    }

    @Override
    public void save(AccessTokenEntity entity) {
        this.template.save(entity);
    }

    @Override
    public AccessTokenEntity findByRefreshToken(String refreshToken) {
        if (ObjectUtils.isEmpty(refreshToken))
            return null;
        Query query = Query.query(Criteria.where(AccessTokenEntity.Fields.refreshToken).is(refreshToken));
        return this.template.findOne(query, AccessTokenEntity.class);
    }

    @Override
    public AccessTokenEntity findByAuthenticationId(String authenticationId) {
        if (ObjectUtils.isEmpty(authenticationId))
            return null;
        Query query = Query.query(Criteria.where(AccessTokenEntity.Fields.authenticationId).is(authenticationId));
        return this.template.findOne(query, AccessTokenEntity.class);
    }

    @Override
    public List<AccessTokenEntity> findByClientId(String clientId) {
        if (ObjectUtils.isEmpty(clientId))
            return null;
        Query query = Query.query(Criteria.where(AccessTokenEntity.Fields.authenticationId).is(clientId));
        return this.template.find(query, AccessTokenEntity.class);
    }

    @Override
    public List<AccessTokenEntity> findByClientIdAndUsername(String clientId, String username) {
        if (ObjectUtils.anyNull(clientId, username))
            return null;
        Query query = Query.query(Criteria.where(AccessTokenEntity.Fields.authenticationId).is(clientId));
        query.addCriteria(Criteria.where(AccessTokenEntity.Fields.username).is(username));
        return this.template.find(query, AccessTokenEntity.class);
    }

    @Override
    public void delete(AccessTokenEntity entity) {
        this.template.remove(entity);
    }
}