package com.sample.spring.repository.impl;

import com.sample.spring.common.model.IdentityEntity;
import com.sample.spring.entity.UserEntity;
import com.sample.spring.enums.StatusType;
import com.sample.spring.mapper.UserMapper;
import com.sample.spring.repository.BasicMongoRepository;
import com.sample.spring.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserRepositoryImpl extends BasicMongoRepository<UserEntity> implements UserRepository {
    private final MongoTemplate template;

    protected UserRepositoryImpl(MongoTemplate template, Class<UserEntity> clazz) {
        super(template, clazz);
        this.template = template;
    }

    @Override
    public UserEntity findByUsername(String username) {
        if (ObjectUtils.isEmpty(username))
            return null;
        return this.findByField(Map.of(UserEntity.Fields.status, String.valueOf(StatusType.ACTIVE), UserEntity.Fields.username, username));
    }

    @Override
    public UserEntity findOneById(String id) {
        if (ObjectUtils.isEmpty(id))
            return null;
        return this.findByField(IdentityEntity.Fields.id, id);
    }

    @Override
    public void save(UserEntity entity) {
        this.create(entity);
    }

    @Override
    public void copyObjectToObject(UserEntity entityA, UserEntity entityB) {
        UserMapper.INSTANCE.entityToEntity(entityA, entityB);
    }
}
