package com.sample.spring.repository.impl;

import com.sample.spring.entity.ClientDetailEntity;
import com.sample.spring.enums.StatusType;
import com.sample.spring.mapper.ClientDetailMapper;
import com.sample.spring.repository.BasicMongoRepository;
import com.sample.spring.repository.ClientDetailRepository;
import lombok.SneakyThrows;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ClientDetailRepositoryImpl extends BasicMongoRepository<ClientDetailEntity> implements ClientDetailRepository {
    private final MongoTemplate template;

    protected ClientDetailRepositoryImpl(MongoTemplate template, Class<ClientDetailEntity> clazz) {
        super(template, clazz);
        this.template = template;
    }

    @Override
    public ClientDetailEntity findClientByClientId(String clientId) {
        return this.findByField(Map.of(ClientDetailEntity.Fields.status, String.valueOf(StatusType.ACTIVE), ClientDetailEntity.Fields.clientId, clientId));
    }

    @Override
    @SneakyThrows
    public void update(String clientId, ClientDetailEntity entity) {
        this.update(ClientDetailEntity.Fields.clientId, clientId, entity);
    }

    @Override
    public void copyObjectToObject(ClientDetailEntity entityA, ClientDetailEntity entityB) {
        ClientDetailMapper.INSTANCE.entityToEntity(entityA, entityB);
    }
}
