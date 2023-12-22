package com.sample.spring.repository.impl;

import com.sample.spring.common.model.IdentityEntity;
import com.sample.spring.common.model.ModelQueryDto;
import com.sample.spring.common.model.QueryCondition;
import com.sample.spring.entity.UserEntity;
import com.sample.spring.enums.StatusType;
import com.sample.spring.repository.BasicMongoRepository;
import com.sample.spring.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl extends BasicMongoRepository<UserEntity> implements UserRepository {
    @Autowired
    protected UserRepositoryImpl(MongoTemplate template) {
        super(template);
    }

    @Override
    public UserEntity findByUsername(String username) {
        if (ObjectUtils.isEmpty(username))
            return null;
        List<ModelQueryDto> modelQueries = new ArrayList<>();
        modelQueries.add(new ModelQueryDto(UserEntity.Fields.status, QueryCondition.IS, String.valueOf(StatusType.ACTIVE)));
        modelQueries.add(new ModelQueryDto(UserEntity.Fields.username, QueryCondition.IS, username));
        return this.findByField(modelQueries);
    }

    @Override
    public UserEntity findOneById(String id) {
        if (ObjectUtils.isEmpty(id))
            return null;
        return this.findByField(new ModelQueryDto(IdentityEntity.Fields.id, QueryCondition.IS, id));
    }

    @Override
    public void save(UserEntity entity) {
        this.create(entity);
    }

}
