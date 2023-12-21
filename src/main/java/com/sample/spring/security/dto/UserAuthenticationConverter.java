package com.sample.spring.security.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Map;

@Slf4j
public class UserAuthenticationConverter extends DefaultUserAuthenticationConverter {
    private final ObjectMapper mapper;

    public UserAuthenticationConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = (Map<String, Object>) super.convertUserAuthentication(authentication);
        try {
            if (authentication instanceof AuthenticationDto && authentication.getPrincipal() instanceof UserEntity) {
                UserEntity user = (UserEntity) authentication.getPrincipal();
                response.put("user_id", user.getId());
            }
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            response.putIfAbsent("setting", details.get("setting"));
            response.putIfAbsent("wing_platform", details.get("wing_platform"));
            response.putIfAbsent("client_secret", details.get("client_secret"));
            response.putIfAbsent("user_full_name", details.get("user_full_name"));
        } catch (Exception e) {
            log.warn("check access token exception occurred when convert user authentication {}", e.getMessage());
        }
        return response;
    }

}
