package com.sample.spring.repository.impl;

import com.sample.spring.common.model.IdentityEntity;
import com.sample.spring.common.model.ModelQueryDto;
import com.sample.spring.common.model.QueryCondition;
import com.sample.spring.entity.RefreshTokenEntity;
import com.sample.spring.repository.BasicMongoRepository;
import com.sample.spring.repository.RefreshTokenRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepositoryImpl extends BasicMongoRepository<RefreshTokenEntity> implements RefreshTokenRepository {
    @Autowired
    protected RefreshTokenRepositoryImpl(MongoTemplate template) {
        super(template);
    }

    @Override
    public RefreshTokenEntity findByTokenId(String tokenId) {
        if (ObjectUtils.isEmpty(tokenId))
            return null;
        return this.findByField(new ModelQueryDto(RefreshTokenEntity.Fields.tokenId, QueryCondition.IS, tokenId));
    }

    @Override
    public void delete(RefreshTokenEntity entity) {
        if (ObjectUtils.isEmpty(entity.getId()))
            return;
        this.removeByField(new ModelQueryDto(IdentityEntity.Fields.id, QueryCondition.IS, entity.getId()));
    }
}