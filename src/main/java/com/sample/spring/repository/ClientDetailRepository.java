package com.sample.spring.repository;


import com.sample.spring.entity.ClientDetailEntity;

public interface ClientDetailRepository {

    ClientDetailEntity findClientByClientId(String clientId);

    void create(ClientDetailEntity entity);

    void update(String clientId, ClientDetailEntity entity);
}
