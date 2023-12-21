package com.sample.spring.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.sample.spring.common.exception.FatalSecurityException;
import com.sample.spring.common.util.ContextUtil;
import com.sample.spring.common.util.I18nUtils;
import com.sample.spring.security.OAuth2ResponseExceptionTranslator;
import com.sample.spring.security.dto.AuthenticationDto;
import com.sample.spring.security.dto.AuthenticationProviderDao;
import com.sample.spring.security.dto.AuthenticationProviderDto;
import com.sample.spring.security.dto.OAuth2ExceptionResponse;
import com.sample.spring.service.ClientDetailService;
import com.sample.spring.service.impl.UserDetailServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;

@Configuration
@EntityScan(basePackages = "com.sample.spring.entity")
public class AppConfiguration implements ApplicationListener<ContextRefreshedEvent>, WebMvcConfigurer {
    @Bean
    public DecimalFormat decimalFormat() {
        return new DecimalFormat("#.00");
    }

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder mapperBuilder) {
        ObjectMapper mapper = mapperBuilder.build();
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        mapper.setSerializerProvider(new DefaultSerializerProvider());
        return mapper;
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, ClientDetailsService clientDetailsService, UserDetailServiceImpl userDetailsService, ClientDetailService clientDetailService) {
        AuthenticationProviderDto provider = new AuthenticationProviderDto();
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

    @Bean
    public LocaleResolver sessionLocaleResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("en"));
        localeResolver.setSupportedLocales(Arrays.asList(new Locale("km"), new Locale("en"), new Locale("kr")));
        return localeResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        /* /src/main/resources/i18ns */
        source.setBasename("i18ns/messages");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        overrideResponseExceptionTranslator();//dirty override oauth2 response exception translator
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
//                    entryPoint.setExceptionTranslator(webResponseExceptionTranslator());
                    entryPoint.setExceptionTranslator(new OAuth2ResponseExceptionTranslator());
                });
    }


//    @Bean
//    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
//        return new DefaultWebResponseExceptionTranslator() {
//            private static final String RESULT = "result";
//            private static final String STATUS = "status";
//            private static final String TRACE_ID = "trace_id";
//            private static final String CODE = "code";
//            private static final String MESSAGE = "message";
//            private static final String PATH = "path";
//            private static final String EXCEPTION = "exception";
//            private static final String DESCRIPTION = "description";
//
//            @Override
//            public ResponseEntity<OAuth2Exception> translate(Exception ex) throws Exception {
//                ResponseEntity<OAuth2Exception> responseEntity = super.translate(ex);
//                HttpHeaders headers = new HttpHeaders();
//                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
//                Map<String, Object> response = new HashMap<>();
//                response.put(RESULT, I18nUtils.messageResolver("FAILED", "Failed"));
//                ContextUtil.getTraceContext().ifPresent(v -> response.put(TRACE_ID, v.getTraceId()));
//                response.put(STATUS, String.valueOf(responseEntity.getStatusCode().value()));
//                String code = String.format("E0%s", responseEntity.getStatusCode().value());
//                Map<String, Object> error = new HashMap<>();
//                error.put(CODE, code);
//                String exception = ex.getClass().getSimpleName();
//                String message = ex.getLocalizedMessage();
//                if (ex instanceof FatalSecurityException) {
//                    error.put(DESCRIPTION, ((FatalSecurityException) ex).getDescription());
//                    error.put(PATH, ((FatalSecurityException) ex).getDescription());
//                } else if (ex instanceof InvalidTokenException) {
//                    if ("Token has expired".equals(ex.getMessage()) || (StringUtils.isNotEmpty(ex.getMessage()) && ex.getMessage().contains("Access token expired"))) {
//                        error.put(DESCRIPTION, "Access token has expired");
//                    } else if (StringUtils.isNotEmpty(ex.getMessage()) && ex.getMessage().contains("Client not valid")) {
//                        String exMsg = ex.getMessage();
//                        String clientId = exMsg.substring(exMsg.indexOf(":") + 2);
//                        error.put(DESCRIPTION, "Client " + clientId + " not found");
//                    } else {
//                        error.put(DESCRIPTION, "Invalid access token");
//                    }
//                } else if (ex instanceof InvalidGrantException)
//                    error.put(DESCRIPTION, "Unauthorized access");
//                else if (ex instanceof BadCredentialsException)
//                    error.put(DESCRIPTION, "Incorrect username or password");
//                else if (ex instanceof OAuth2Exception) {
//                    OAuth2Exception e = (OAuth2Exception) ex;
//                    error.put(DESCRIPTION, e.getMessage());
//                } else if (ex instanceof UsernameNotFoundException)
//                    error.put(DESCRIPTION, ex.getMessage());
//                else if (ex instanceof AuthenticationException) {
//                    AuthenticationException e = (AuthenticationException) ex;
//                    error.put(DESCRIPTION, e.getMessage());
//                } else if (ex instanceof AccessDeniedException)
//                    error.put(DESCRIPTION, ex.getMessage());
//                error.put(EXCEPTION, exception);
//                error.put(MESSAGE, message);
//                if (ContextUtil.isProfile("dev", "local", "test", "debug"))
//                    response.put("error", error);
//                return ResponseEntity.status(HttpStatus.valueOf(responseEntity.getStatusCode().value())).body(new OAuth2ExceptionResponse(response));
//            }
//        };
//    }
}