package com.sample.spring.service.impl;

import com.sample.spring.entity.UserEntity;
import com.sample.spring.security.dto.AuthenticationDto;
import com.sample.spring.security.dto.OAuth2AccessTokenDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TokenEnhancerImpl implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        OAuth2AccessTokenDto token = new OAuth2AccessTokenDto(accessToken, authentication);
        Map<String, Object> map = new HashMap<>();
        map.put("authorities", authentication.getAuthorities().parallelStream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserEntity) {
            map.put("user_id", ((UserEntity) principal).getId());
        }
        if (authentication.getUserAuthentication() instanceof AuthenticationDto && principal instanceof UserEntity) {
            AuthenticationDto auth = (AuthenticationDto) authentication.getUserAuthentication();
            UserEntity userEntity = (UserEntity) principal;
        }
        token.setAdditionalInformation(map);
        return token;
    }
}
