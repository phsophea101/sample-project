package com.sample.spring.repository.impl;

import com.sample.spring.common.model.ModelQueryDto;
import com.sample.spring.common.model.QueryCondition;
import com.sample.spring.entity.ClientDetailEntity;
import com.sample.spring.enums.StatusType;
import com.sample.spring.repository.BasicMongoRepository;
import com.sample.spring.repository.ClientDetailRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientDetailRepositoryImpl extends BasicMongoRepository<ClientDetailEntity> implements ClientDetailRepository {
    @Autowired
    protected ClientDetailRepositoryImpl(MongoTemplate template) {
        super(template);
    }

    @Override
    public ClientDetailEntity findClientByClientId(String clientId) {
        List<ModelQueryDto> queries = new ArrayList<>();
        queries.add(new ModelQueryDto(ClientDetailEntity.Fields.status, QueryCondition.IS, String.valueOf(StatusType.ACTIVE)));
        queries.add(new ModelQueryDto(ClientDetailEntity.Fields.clientId, QueryCondition.IS, clientId));
        return this.findByField(queries);
    }

    @Override
    @SneakyThrows
    public void update(String clientId, ClientDetailEntity entity) {
        this.update(entity);
    }

}
