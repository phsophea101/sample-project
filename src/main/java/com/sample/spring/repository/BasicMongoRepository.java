package com.sample.spring.repository;

import com.sample.spring.common.exception.BizException;
import com.sample.spring.common.model.ModelQueryDto;
import com.sample.spring.enums.BizErrorCode;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import java.util.List;

public abstract class BasicMongoRepository<T> implements BasicRepository<T> {

    protected final MongoTemplate template;

    protected BasicMongoRepository(MongoTemplate template) {
        Assert.notNull(template, "Template must not be null");
        this.template = template;
    }

    @Override
    public void create(T entity) {
        this.template.save(entity);
    }

    @SneakyThrows
    @Override
    public void update(T entity) {
        if (ObjectUtils.isEmpty(entity))
            throw new BizException(BizErrorCode.E0002);
        this.create(entity);
    }

    @Override
    public void removeByField(ModelQueryDto modelQuery) {
        Query query = Query.query(this.generateQuery(modelQuery));
        this.template.remove(query);
    }

    @Override
    public T findByField(ModelQueryDto modelQuery) {
        Query query = Query.query(this.generateQuery(modelQuery));
        return this.template.findOne(query, this.getEntityClass());
    }

    @Override
    public T findByField(List<ModelQueryDto> modelQueries) {
        Query query = new Query();
        for (ModelQueryDto modelQuery : modelQueries) {
            query.addCriteria(this.generateQuery(modelQuery));
        }
        return this.template.findOne(query, this.getEntityClass());
    }

    @Override
    public List<T> findMany(ModelQueryDto modelQuery) {
        Query query = Query.query(this.generateQuery(modelQuery));
        return this.template.find(query, this.getEntityClass());
    }

    @Override
    public List<T> findMany(List<ModelQueryDto> modelQueries) {
        Query query = new Query();
        for (ModelQueryDto modelQuery : modelQueries) {
            query.addCriteria(this.generateQuery(modelQuery));
        }
        return this.template.find(query, this.getEntityClass());
    }

    private Criteria generateQuery(ModelQueryDto modelQuery) {
        Criteria criteria = new Criteria();
        switch (modelQuery.getCondition()) {
            case IS:
                criteria = Criteria.where(modelQuery.getFieldName()).is(modelQuery.getFieldValue());
                break;
            case LIKE:
                criteria = Criteria.where(modelQuery.getFieldName()).regex(String.valueOf(modelQuery.getFieldValue()));
                break;
        }
        return criteria;
    }

}
