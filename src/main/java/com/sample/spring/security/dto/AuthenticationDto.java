package com.sample.spring.security.dto;

import com.sample.spring.dto.ClientDetailDto;
import com.sample.spring.entity.ClientDetailEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class AuthenticationDto extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = 4271981202448136517L;

    private ClientDetailDto clientDetails;

    public AuthenticationDto(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AuthenticationDto(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public void setClientDetails(ClientDetailDto clientDetails) {
        this.clientDetails = clientDetails;
    }

    public ClientDetailDto getClientDetails() {
        return clientDetails;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getParameter(String name) {
        Map<String, Object> details = (Map<String, Object>) getDetails();
        return details != null ? Optional.ofNullable((T) details.get(name)) : Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAdditional(String name) {
        Map<String, Object>  additionalInfo = clientDetails.getAdditionalInformation();
        return Optional.ofNullable((T) additionalInfo.get(name));
    }
}
