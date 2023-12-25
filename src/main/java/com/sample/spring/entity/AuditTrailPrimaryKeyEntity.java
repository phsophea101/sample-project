package com.sample.spring.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@Document(collection = AuditTrailFieldEntity.TABLE_NAME)
public class AuditTrailPrimaryKeyEntity {
    public static final String TABLE_NAME = "audit_trail_primary_keys";
    @Field("field_name")
    private String fieldName;
    private String value;
}
