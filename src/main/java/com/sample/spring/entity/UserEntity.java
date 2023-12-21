package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
@Document(collection = UserEntity.TABLE_NAME)
@FieldNameConstants
public class UserEntity extends AuditEntity implements RecyclableEntity<String>, UserDetails {
    public static final String TABLE_NAME = "users";
    @Indexed(unique = true)
    private String username;
    @Indexed
    private String password;
    private List<RoleEntity> roles;
    @Indexed
    private String status = String.valueOf(StatusType.ACTIVE);
    private boolean locked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isLocked();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return String.valueOf(StatusType.ACTIVE).equalsIgnoreCase(status);
    }

    @Override
    public boolean isEnabled() {
        return String.valueOf(StatusType.ACTIVE).equalsIgnoreCase(status);
    }
}
