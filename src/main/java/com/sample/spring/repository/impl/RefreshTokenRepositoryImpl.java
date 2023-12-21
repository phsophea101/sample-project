package com.sample.spring.repository.impl;

import com.sample.spring.entity.RefreshTokenEntity;
import com.sample.spring.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final MongoTemplate template;

    @Override
    public RefreshTokenEntity findByTokenId(String tokenId) {
        if (ObjectUtils.isEmpty(tokenId))
            return null;
        Query query = Query.query(Criteria.where(RefreshTokenEntity.Fields.tokenId).is(tokenId));
        return this.template.findOne(query, RefreshTokenEntity.class);
    }

    @Override
    public void save(RefreshTokenEntity entity) {
        this.template.save(entity);
    }

    @Override
    public void delete(RefreshTokenEntity entity) {
        this.template.remove(entity);
    }
}