package com.sample.spring.entity;

import com.sample.spring.audit.dto.AuditTrailFieldDto;
import com.sample.spring.audit.dto.AuditTrailPrimaryKeyDto;
import com.sample.spring.audit.dto.RoleDto;
import com.sample.spring.common.model.AuditEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Document(collection = AuditTrailEntity.TABLE_NAME)
public class AuditTrailEntity extends AuditEntity {
    public static final String TABLE_NAME = "audit_trails";
    @Field("trace_id")
    private String traceId;
    @Field("table_name")
    private String tableName;
    private String user;
    private String action;
    @Field("service_name")
    private String serviceName;
    private List<RoleDto> roles;
    private List<AuditTrailFieldDto> fields;
    @Field("primary_keys")
    private List<AuditTrailPrimaryKeyDto> primaryKeys;
}
