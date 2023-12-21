package com.sample.spring.security;

import com.sample.spring.dto.ClientDetailDto;
import com.sample.spring.entity.UserEntity;
import com.sample.spring.security.dto.AuthenticationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import java.util.Map;

@Slf4j
public class AuthenticationHelper {

    public static Authentication createSuccessAuthentication(UserEntity user, ClientDetailDto client, Map<String, Object> details) {
        Assert.notNull(user, "User entity cannot be null");
        Assert.notNull(client, "Client detail entity cannot be null");
        Assert.notNull(details, "Details map cannot be null");
        details.put("client_secret", client.getClientSecret());
        AuthenticationDto authenticationAce = new AuthenticationDto(user, user.getPassword(), user.getRoles());
        authenticationAce.setDetails(details);
        authenticationAce.setClientDetails(client);
        return authenticationAce;
    }

}
