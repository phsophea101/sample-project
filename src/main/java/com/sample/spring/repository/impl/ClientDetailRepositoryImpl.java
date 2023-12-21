package com.sample.spring.repository.impl;

import com.sample.spring.entity.ClientDetailEntity;
import com.sample.spring.enums.StatusType;
import com.sample.spring.repository.ClientDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ClientDetailRepositoryImpl implements ClientDetailRepository {
    private final MongoTemplate template;

    @Override
    public ClientDetailEntity findClientByClientId(String clientId) {
        Query query = Query.query(Criteria.where(ClientDetailEntity.Fields.status).is(String.valueOf(StatusType.ACTIVE)));
        query.addCriteria(Criteria.where(ClientDetailEntity.Fields.clientId).is(clientId));
        return this.template.findOne(query, ClientDetailEntity.class);
    }
}
