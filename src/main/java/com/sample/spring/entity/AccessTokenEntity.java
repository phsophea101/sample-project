package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.io.Serializable;

@Getter
@Setter
@FieldNameConstants
@Document(collection = AccessTokenEntity.TABLE_NAME)
public class AccessTokenEntity extends AuditEntity implements RecyclableEntity<String>, Serializable {
    public static final String TABLE_NAME = "access_tokens";
    @Field("token_id")
    private String tokenId;
    @Field("oauth2_access_token")
    private OAuth2AccessToken oAuth2AccessToken;
    @Field("authentication_id")
    private String authenticationId;
    private String username;
    @Field("client_id")
    private String clientId;
    private OAuth2Authentication authentication;
    @Field("refresh_token")
    private String refreshToken;
    @Indexed
    private String status = String.valueOf(StatusType.ACTIVE);
}
