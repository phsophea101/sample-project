package com.sample.spring.repository.impl;

import com.sample.spring.common.model.IdentityEntity;
import com.sample.spring.entity.UserEntity;
import com.sample.spring.enums.StatusType;
import com.sample.spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final MongoTemplate template;

    @Override
    public UserEntity findByUsername(String username) {
        if (ObjectUtils.isEmpty(username))
            return null;
        Query query = Query.query(Criteria.where(UserEntity.Fields.status).is(String.valueOf(StatusType.ACTIVE)));
        query.addCriteria(Criteria.where(UserEntity.Fields.username).is(username));
        return this.template.findOne(query, UserEntity.class);
    }

    @Override
    public UserEntity findOneById(String id) {
        if (ObjectUtils.isEmpty(id))
            return null;
        Query query = Query.query(Criteria.where(IdentityEntity.Fields.id).is(id));
        return this.template.findOne(query, UserEntity.class);
    }

    @Override
    public void save(UserEntity entity) {
        this.template.save(entity);
    }
}
