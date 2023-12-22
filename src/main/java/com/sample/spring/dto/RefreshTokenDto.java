package com.sample.spring.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Setter
@Getter
public class RefreshTokenDto {
    private String tokenId;
    private OAuth2RefreshToken oAuth2RefreshToken;
    private OAuth2Authentication authentication;
}
