package com.sample.spring.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class OAuth2AccessTokenDto extends DefaultOAuth2AccessToken {
    private OAuth2Authentication authentication;

    public OAuth2AccessTokenDto(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        super(accessToken);
        this.authentication = authentication;
    }

    public void setAuthentication(OAuth2Authentication authentication) {
        this.authentication = authentication;
    }

    @JsonIgnore
    public Authentication getAuthentication() {
        return authentication.getUserAuthentication();
    }

    @JsonIgnore
    public OAuth2Authentication getOAuth2Authentication() {
        return authentication;
    }

    @JsonIgnore
    public Object getPrincipal() {
        return authentication.getPrincipal();
    }
}
