package com.sample.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sample.spring.common.model.AuditEntity;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@FieldNameConstants
@Document(collection = ClientDetailEntity.TABLE_NAME)
public class ClientDetailEntity extends AuditEntity implements RecyclableEntity<String>, Serializable {
    public static final String TABLE_NAME = "oauth_client_details";
    @Field(name = "client_id")
    private String clientId;
    @Field(name = "client_secret")
    private String clientSecret;
    private Boolean shareToken = Boolean.FALSE;
    private Boolean checkPassword = Boolean.TRUE;
    private String usernameConfiguration;
    private Set<String> scope = Collections.emptySet();

    @Field("resource_ids")
    private Set<String> resourceIds = Collections.emptySet();

    @Field("authorized_grant_types")
    private Set<String> authorizedGrantTypes = Collections.emptySet();

    @Field("redirect_uri")
    private Set<String> registeredRedirectUris;

    @Field("auto_approve")
    private Set<String> autoApproveScopes;

    private List<GrantedAuthority> authorities;

    @Field("access_token_validity")
    private Integer accessTokenValiditySeconds;

    @Field("refresh_token_validity")
    private Integer refreshTokenValiditySeconds;

    @JsonIgnore
    private Map<String, Object> additionalInformation = new LinkedHashMap<>();
    @Indexed
    private String status = String.valueOf(StatusType.ACTIVE);
}
