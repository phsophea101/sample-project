package com.sample.spring.repository;

import com.sample.spring.common.model.ModelQueryDto;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public interface BasicRepository<T> {

    default Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    void create(T entity);

    void update(T entity);

    void removeByField(ModelQueryDto modelQuery);

    T findByField(ModelQueryDto modelQuery);

    T findByField(List<ModelQueryDto> modelQueries);

    List<T> findMany(ModelQueryDto modelQuery);

    List<T> findMany(List<ModelQueryDto> modelQueries);

}
