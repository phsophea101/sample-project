package com.sample.spring.security.dto;

import com.sample.spring.common.exception.BizException;
import com.sample.spring.common.exception.FatalSecurityException;
import com.sample.spring.common.util.LazyMapUtils;
import com.sample.spring.dto.ClientDetailDto;
import com.sample.spring.entity.UserEntity;
import com.sample.spring.enums.BizErrorCode;
import com.sample.spring.repository.ClientDetailRepository;
import com.sample.spring.security.AuthenticationHelper;
import com.sample.spring.service.ClientDetailService;
import com.sample.spring.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AuthenticationProviderDto implements AuthenticationProvider, InitializingBean {


    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private ClientDetailService clientDetailService;

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    public void setClientDetailRepository(ClientDetailService clientDetailService) {
        this.clientDetailService = clientDetailService;
    }


    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!StringUtils.hasText(authentication.getName())) throw new BizException(BizErrorCode.E0004);
        Map<String, Object> details = (HashMap<String, Object>) Optional.ofNullable(authentication.getDetails()).orElse(new HashMap<>());
        UsernamePasswordAuthenticationToken clientAuthentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        ClientDetailDto client = Optional.ofNullable(this.clientDetailService.findClientByClientId(clientAuthentication.getName())).orElseThrow(() -> new BizException(BizErrorCode.E0005));
        clientAuthentication.setDetails(new LazyMapUtils("detail", clientAuthentication.getDetails()).append("client", client));

        if (Boolean.TRUE.equals(client.getCheckPassword()) && authentication.getCredentials() == null)
            throw new BizException(BizErrorCode.E0006);

        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(authentication.getName());
        if (Boolean.TRUE.equals(client.getCheckPassword()) && !passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BizException(BizErrorCode.E0007);
        }
        if (CollectionUtils.isEmpty(user.getRoles()))
            throw new BizException(BizErrorCode.E0008);
        return AuthenticationHelper.createSuccessAuthentication(user, client, details);
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(userDetailsService, "User detail service is required");
        Assert.notNull(passwordEncoder, "Password encoder is required");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}