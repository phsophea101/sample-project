package com.sample.spring.repository.impl;

import com.sample.spring.common.model.IdentityEntity;
import com.sample.spring.entity.RefreshTokenEntity;
import com.sample.spring.mapper.RefreshTokenMapper;
import com.sample.spring.repository.BasicMongoRepository;
import com.sample.spring.repository.RefreshTokenRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepositoryImpl extends BasicMongoRepository<RefreshTokenEntity> implements RefreshTokenRepository {
    private final MongoTemplate template;

    protected RefreshTokenRepositoryImpl(MongoTemplate template, Class<RefreshTokenEntity> clazz) {
        super(template, clazz);
        this.template = template;
    }

    @Override
    public RefreshTokenEntity findByTokenId(String tokenId) {
        if (ObjectUtils.isEmpty(tokenId))
            return null;
        return this.findByField(RefreshTokenEntity.Fields.tokenId, tokenId);
    }

    @Override
    public void delete(RefreshTokenEntity entity) {
        if (ObjectUtils.isEmpty(entity.getId()))
            return;
        this.removeByField(IdentityEntity.Fields.id, entity.getId());
    }

    @Override
    public void copyObjectToObject(RefreshTokenEntity entityA, RefreshTokenEntity entityB) {
        RefreshTokenMapper.INSTANCE.entityToEntity(entityA, entityB);
    }
}