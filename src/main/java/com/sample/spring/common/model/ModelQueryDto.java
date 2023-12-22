package com.sample.spring.common.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModelQueryDto {
    private String fieldName;
    private QueryCondition condition;
    private Object fieldValue;
    private Integer rpp = 10;
    private Integer page = 1;
    private Integer offset;

    public int getOffset() {
        return (getPage() - 1) * getRpp();
    }

    public ModelQueryDto(String fieldName, QueryCondition condition, Object fieldValue, Integer rpp, Integer page) {
        this.fieldName = fieldName;
        this.condition = condition;
        this.fieldValue = fieldValue;
        this.rpp = rpp;
        this.page = page;
    }

    public ModelQueryDto(String fieldName, QueryCondition condition, Object fieldValue) {
        this.fieldName = fieldName;
        this.condition = condition;
        this.fieldValue = fieldValue;
    }
}
