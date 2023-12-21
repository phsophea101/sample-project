package com.sample.spring.security;

import com.sample.spring.security.dto.OAuth2AccessTokenDto;
import com.sample.spring.service.impl.TokenStoreServiceImpl;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

public class SoleResourceServerTokenService implements ResourceServerTokenServices {

    private final AuthorizationServerEndpointsConfigurer endpoint;
    private final TokenStoreServiceImpl tokenStore;

    private SoleResourceServerTokenService(AuthorizationServerEndpointsConfigurer endpoint, TokenStoreServiceImpl tokenStore) {
        Assert.notNull(endpoint, "Authorization server endpoint must not null");
        Assert.notNull(tokenStore, "Sole jdbc token store must not null");
        this.endpoint = endpoint;
        this.tokenStore = tokenStore;
    }

    public static void override(AuthorizationServerEndpointsConfigurer endpoint, TokenStoreServiceImpl tokenStore) {
        SoleResourceServerTokenService tokenService = new SoleResourceServerTokenService(endpoint, tokenStore);
        Field field = ReflectionUtils.findField(AuthorizationServerEndpointsConfigurer.class, "resourceTokenServices");
        if (Objects.nonNull(field)) {
            field.setAccessible(true);
            ReflectionUtils.setField(field, endpoint, tokenService);
        }
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        DefaultTokenServices defaultTokenServices = (DefaultTokenServices) this.endpoint.getDefaultAuthorizationServerTokenServices();
        return defaultTokenServices.loadAuthentication(accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        DefaultTokenServices defaultTokenServices = (DefaultTokenServices) this.endpoint.getDefaultAuthorizationServerTokenServices();
        OAuth2AccessToken auth2AccessToken = defaultTokenServices.readAccessToken(accessToken);
        if (auth2AccessToken instanceof OAuth2AccessTokenDto)
            this.tokenStore.revokeOtherAccessTokens(((OAuth2AccessTokenDto) auth2AccessToken).getOAuth2Authentication(), accessToken);
        return auth2AccessToken;
    }
}
