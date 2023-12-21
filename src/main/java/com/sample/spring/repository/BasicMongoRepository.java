package com.sample.spring.repository;

import com.sample.spring.common.exception.BizException;
import com.sample.spring.enums.BizErrorCode;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

public abstract class BasicMongoRepository<T> implements BasicRepository<T> {

    protected final MongoTemplate template;
    protected final Class<T> clazz;

    protected BasicMongoRepository(MongoTemplate template, Class<T> clazz) {
        Assert.notNull(template, "Template must not be null");
        Assert.notNull(clazz, "Clazz must not be null");
        this.clazz = clazz;
        this.template = template;
    }

    @Override
    public void create(T entity) {
        this.template.save(entity);
    }

    @SneakyThrows
    @Override
    public void update(String fieldName, Object fieldValue, T entity) {
        T detailEntity = this.findByField(fieldName, fieldValue);
        if (ObjectUtils.isEmpty(detailEntity))
            throw new BizException(BizErrorCode.E0002);
        this.copyObjectToObject(entity, detailEntity);
        this.create(detailEntity);
        this.copyObjectToObject(detailEntity, entity);
    }

    @SneakyThrows
    @Override
    public void update(Map<String, Object> keyValues, T entity) {
        T detailEntity = this.findByField(keyValues);
        if (ObjectUtils.isEmpty(detailEntity))
            throw new BizException(BizErrorCode.E0002);
        this.copyObjectToObject(entity, detailEntity);
        this.create(detailEntity);
        this.copyObjectToObject(detailEntity, entity);
    }

    @Override
    public void removeByField(String fieldName, Object fieldValue) {
        Query query = Query.query(Criteria.where(fieldName).is(fieldValue));
        this.template.remove(query);
    }

    @Override
    public T findByField(String fieldName, Object fieldValue) {
        Query query = Query.query(Criteria.where(fieldName).is(fieldValue));
        return this.template.findOne(query, this.clazz);
    }

    @Override
    public T findByField(Map<String, Object> keyValues) {
        Query query = new Query();
        for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
            query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
        }
        return this.template.findOne(query, this.clazz);
    }

    @Override
    public List<T> findMany(String fieldName, Object fieldValue) {
        Query query = Query.query(Criteria.where(fieldName).is(fieldValue));
        return this.template.find(query, this.clazz);
    }

    @Override
    public List<T> findMany(Map<String, Object> keyValues) {
        Query query = new Query();
        for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
            query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
        }
        return this.template.find(query, this.clazz);
    }

    @Override
    public List<T> findMany(Query query) {
        return this.template.find(query, this.clazz);
    }
}
