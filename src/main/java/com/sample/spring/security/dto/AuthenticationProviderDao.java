package com.sample.spring.security.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Predicate;

@Slf4j
public class AuthenticationProviderDao extends DaoAuthenticationProvider {


    protected Predicate<Class<?>> support;

    public AuthenticationProviderDao() {
    }

    public AuthenticationProviderDao(Predicate<Class<?>> support) {
        this.support = support;
    }

    public AuthenticationProviderDao(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken credential) throws AuthenticationException {
        try {
            super.additionalAuthenticationChecks(userDetails, credential);
        } catch (AuthenticationException e) {
            log.debug(e.getMessage(), e);
            throw new BadCredentialsException("Bad credentials");
        }
    }

    public void setSupport(Predicate<Class<?>> support) {
        this.support = support;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return support == null ? super.supports(authentication) : support.test(authentication);
    }
}
