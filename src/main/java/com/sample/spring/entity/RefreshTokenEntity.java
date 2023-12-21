package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.io.Serializable;

@Setter
@Getter
@FieldNameConstants
@Document(collection = RefreshTokenEntity.TABLE_NAME)
public class RefreshTokenEntity extends AuditEntity implements RecyclableEntity<String>, Serializable {
    public static final String TABLE_NAME = "refresh_tokens";
    @Field("token_id")
    private String tokenId;
    @Field("oauth2_refresh_token")
    private OAuth2RefreshToken oAuth2RefreshToken;
    private OAuth2Authentication authentication;
    private String status = String.valueOf(StatusType.ACTIVE);
}
