package com.sample.spring.security.config;

import com.sample.spring.common.util.ContextUtil;
import com.sample.spring.security.ClientDetailsServiceImpl;
import com.sample.spring.security.OAuth2ResponseExceptionTranslator;
import com.sample.spring.security.SoleResourceServerTokenService;
import com.sample.spring.security.dto.UserAuthenticationConverter;
import com.sample.spring.service.impl.TokenEnhancerImpl;
import com.sample.spring.service.impl.TokenStoreServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.web.cors.CorsConfigurationSource;

@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private final ClientDetailsServiceImpl clientDetailsService;
    private final TokenStoreServiceImpl tokenStore;
    private final TokenEnhancerImpl tokenEnhancer;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final CorsConfigurationSource corsConfigurationSource;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(this.clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                .tokenStore(this.tokenStore)
                .tokenEnhancer(this.tokenEnhancer)
                .reuseRefreshTokens(Boolean.parseBoolean(ContextUtil.getProperty("reuse.refresh.token", "true")))
                .exceptionTranslator(new OAuth2ResponseExceptionTranslator())
                .authenticationManager(authenticationManager)
                .userDetailsService(this.userDetailsService)
                .getFrameworkEndpointHandlerMapping()
                .setCorsConfigurationSource(this.corsConfigurationSource);
        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) endpoints.getAccessTokenConverter();
        UserAuthenticationConverter converter = new UserAuthenticationConverter();
        accessTokenConverter.setUserTokenConverter(converter);
        SoleResourceServerTokenService.override(endpoints, this.tokenStore);
    }
}
