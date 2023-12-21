package com.sample.spring.dto;

import com.sample.spring.common.model.AuditDto;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDto extends AuditDto implements RecyclableEntity<String> {
    private String id;
    private String username;
    private String password;
    private List<RoleDto> roles;
    private String status = String.valueOf(StatusType.ACTIVE);
}
