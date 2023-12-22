package com.sample.spring.service.impl;

import com.sample.spring.dto.AccessTokenDto;
import com.sample.spring.dto.RefreshTokenDto;
import com.sample.spring.entity.AccessTokenEntity;
import com.sample.spring.entity.RefreshTokenEntity;
import com.sample.spring.mapper.AccessTokenMapper;
import com.sample.spring.mapper.RefreshTokenMapper;
import com.sample.spring.repository.AccessTokenRepository;
import com.sample.spring.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class TokenStoreServiceImpl implements TokenStore {
    private final AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private final AccessTokenRepository accessTokenRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return this.readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        AccessTokenEntity entity = this.accessTokenRepository.findByTokenId(token);
        AccessTokenDto dto = AccessTokenMapper.INSTANCE.entityToDto(entity);
        return ObjectUtils.isEmpty(token) ? null : dto.getAuthentication();
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        OAuth2AccessToken existingToken = readAccessToken(token.getValue());
        if (ObjectUtils.isEmpty(existingToken)) {
            AccessTokenDto dto = new AccessTokenDto();
            dto.setTokenId(token.getValue());
            dto.setAuthentication(authentication);
            dto.setRefreshToken(token.getRefreshToken().getValue());
            dto.setUsername(authentication.getName());
            dto.setClientId(authentication.getOAuth2Request().getClientId());
            dto.setOAuth2AccessToken(token);
            dto.setAuthenticationId(this.authenticationKeyGenerator.extractKey(authentication));
            AccessTokenEntity entity = AccessTokenMapper.INSTANCE.dtoToEntity(dto);
            this.accessTokenRepository.create(entity);
        }
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        AccessTokenEntity entity = this.accessTokenRepository.findByTokenId(tokenValue);
        AccessTokenDto dto = AccessTokenMapper.INSTANCE.entityToDto(entity);
        if (ObjectUtils.isEmpty(dto)) {
            return null;
        }
        return dto.getOAuth2AccessToken();
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        AccessTokenEntity accessToken = this.accessTokenRepository.findByTokenId(token.getValue());
        this.accessTokenRepository.delete(accessToken);
        this.removeRefreshToken(token.getRefreshToken());
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setTokenId(refreshToken.getValue());
        refreshTokenDto.setOAuth2RefreshToken(refreshToken);
        refreshTokenDto.setAuthentication(authentication);
        this.refreshTokenRepository.create(RefreshTokenMapper.INSTANCE.dtoToEntity(refreshTokenDto));
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        RefreshTokenEntity refreshToken = this.refreshTokenRepository.findByTokenId(tokenValue);
        return refreshToken.getOAuth2RefreshToken();
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        RefreshTokenEntity refreshToken = this.refreshTokenRepository.findByTokenId(token.getValue());
        return refreshToken.getAuthentication();
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        RefreshTokenEntity refreshToken = this.refreshTokenRepository.findByTokenId(token.getValue());
        this.refreshTokenRepository.delete(refreshToken);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        AccessTokenEntity accessToken = this.accessTokenRepository.findByRefreshToken(refreshToken.getValue());
        this.accessTokenRepository.delete(accessToken);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        String authenticationId = this.authenticationKeyGenerator.extractKey(authentication);
        if (ObjectUtils.isEmpty(authenticationId)) {
            return null;
        }
        AccessTokenEntity accessToken = this.accessTokenRepository.findByAuthenticationId(authenticationId);
        return ObjectUtils.isEmpty(accessToken) ? null : accessToken.getOAuth2AccessToken();
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
        List<AccessTokenEntity> accessTokens = this.accessTokenRepository.findByClientIdAndUsername(clientId, username);
        return this.extractAccessTokens(AccessTokenMapper.INSTANCE.entityToDtoList(accessTokens));
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<AccessTokenEntity> accessTokens = this.accessTokenRepository.findByClientId(clientId);
        return this.extractAccessTokens(AccessTokenMapper.INSTANCE.entityToDtoList(accessTokens));
    }

    private Collection<OAuth2AccessToken> extractAccessTokens(List<AccessTokenDto> tokens) {
        List<OAuth2AccessToken> accessTokens = new ArrayList<>();
        tokens.forEach(token -> accessTokens.add(token.getOAuth2AccessToken()));
        return accessTokens;
    }

    public void revokeOtherAccessTokens(OAuth2Authentication oAuth2Authentication, String accessToken) {
//    TODO
    }
}
