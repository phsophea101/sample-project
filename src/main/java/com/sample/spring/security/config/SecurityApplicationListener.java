package com.sample.spring.security.config;

import com.sample.spring.common.util.ContextUtil;
import com.sample.spring.security.OAuth2ResponseExceptionTranslator;
import com.sample.spring.security.dto.AuthenticationDto;
import com.sample.spring.security.dto.AuthenticationProviderDao;
import com.sample.spring.security.CustomAuthenticationProvider;
import com.sample.spring.service.ClientDetailService;
import com.sample.spring.service.impl.UserDetailServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Configuration
public class SecurityApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        overrideResponseExceptionTranslator();//dirty override oauth2 response exception translator
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, ClientDetailsService clientDetailsService, UserDetailServiceImpl userDetailsService, ClientDetailService clientDetailService) {
        CustomAuthenticationProvider provider = new CustomAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setClientDetailRepository(clientDetailService);
        AuthenticationProviderDao clientDetailProvider = new AuthenticationProviderDao();
        clientDetailProvider.setPasswordEncoder(passwordEncoder);
        clientDetailProvider.setUserDetailsService(new ClientDetailsUserDetailsService(clientDetailsService));
        clientDetailProvider.setHideUserNotFoundExceptions(false);
        clientDetailProvider.setSupport(AuthenticationDto.class::isAssignableFrom);
        return new ProviderManager(List.of(clientDetailProvider, provider));
    }

    private void overrideResponseExceptionTranslator() {
        ContextUtil.optBean(FilterChainProxy.class)
                .map(FilterChainProxy::getFilterChains)
                .filter(v -> !v.isEmpty())
                .flatMap(v -> Optional.of(v.get(0)))
                .map(SecurityFilterChain::getFilters)
                .flatMap(v -> v.stream().filter(ClientCredentialsTokenEndpointFilter.class::isInstance).findAny()).ifPresent(filter -> {
                    Field field = ReflectionUtils.findField(filter.getClass(), "authenticationEntryPoint");
                    field.setAccessible(true);
                    OAuth2AuthenticationEntryPoint entryPoint = (OAuth2AuthenticationEntryPoint) ReflectionUtils.getField(field, filter);
                    entryPoint.setExceptionTranslator(new OAuth2ResponseExceptionTranslator());
                });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
