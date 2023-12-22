package com.sample.spring.dto;

import com.sample.spring.common.model.AuditDto;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class ClientDetailDto extends AuditDto implements RecyclableEntity<String>, Serializable {
    private Boolean shareToken = Boolean.FALSE;
    private Boolean checkPassword = Boolean.TRUE;
    private String usernameConfiguration;
    private String status = String.valueOf(StatusType.ACTIVE);
    private String clientId;
    private String clientSecret;
    private Set<String> scope = Collections.emptySet();
    private Set<String> resourceIds = Collections.emptySet();
    private Set<String> authorizedGrantTypes = Collections.emptySet();
    private Set<String> registeredRedirectUris;
    private Set<String> autoApproveScopes;
    private List<GrantedAuthority> authorities;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private Map<String, Object> additionalInformation = new LinkedHashMap<>();
}
