package com.sample.spring.repository;

import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

public abstract interface BasicRepository<T> {

    void create(T entity);

    void update(String fieldName, Object fieldValue, T entity);
    void update(Map<String, Object> keyValues, T entity);

    void removeByField(String fieldName, Object fieldValue);

    T findByField(String fieldName, Object fieldValue);
    T findByField(Map<String, Object> keyValues);

    List<T> findMany(String fieldName, Object fieldValue);
    List<T> findMany(Map<String, Object> keyValues);

    List<T> findMany(Query query);

    void copyObjectToObject(T entityA, T entityB);
}
