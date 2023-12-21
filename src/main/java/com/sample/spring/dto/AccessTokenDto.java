package com.sample.spring.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Getter
@Setter
public class AccessTokenDto {
    private String id;
    private String tokenId;
    private OAuth2AccessToken oAuth2AccessToken;
    private String authenticationId;
    private String username;
    private String clientId;
    private OAuth2Authentication authentication;
    private String refreshToken;

}
