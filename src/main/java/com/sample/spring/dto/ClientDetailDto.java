package com.sample.spring.dto;

import com.sample.spring.common.model.AuditDto;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

@Getter
@Setter
public class ClientDetailDto extends AuditDto implements RecyclableEntity<String> {
    private Boolean shareToken = Boolean.FALSE;
    private Boolean checkPassword = Boolean.TRUE;
    private String usernameConfiguration;
    private String status = String.valueOf(StatusType.ACTIVE);
    private String clientId;
    private String clientSecret;
    private Set<String> scope = Collections.emptySet();

    @com.fasterxml.jackson.annotation.JsonProperty("resource_ids")
    private Set<String> resourceIds = Collections.emptySet();

    @com.fasterxml.jackson.annotation.JsonProperty("authorized_grant_types")
    private Set<String> authorizedGrantTypes = Collections.emptySet();

    @com.fasterxml.jackson.annotation.JsonProperty("redirect_uri")
    private Set<String> registeredRedirectUris;

    @com.fasterxml.jackson.annotation.JsonProperty("autoapprove")
    private Set<String> autoApproveScopes;

    private List<GrantedAuthority> authorities;

    @com.fasterxml.jackson.annotation.JsonProperty("access_token_validity")
    private Integer accessTokenValiditySeconds;

    @com.fasterxml.jackson.annotation.JsonProperty("refresh_token_validity")
    private Integer refreshTokenValiditySeconds;

    @com.fasterxml.jackson.annotation.JsonIgnore
    private Map<String, Object> additionalInformation = new LinkedHashMap<>();
}
