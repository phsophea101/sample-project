package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Setter
@Getter
@Document(collection = RoleEntity.TABLE_NAME)
public class RoleEntity extends AuditEntity implements RecyclableEntity<String>, GrantedAuthority {
    public static final String TABLE_NAME = "roles";
    private String code;
    @Field("label_display")
    private String labelDisplay;
    private Set<PermissionEntity> permissions;
    private String status = String.valueOf(StatusType.ACTIVE);

    @Override
    public String getAuthority() {
        return this.getCode();
    }
}
