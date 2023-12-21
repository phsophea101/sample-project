package com.sample.spring.security;

import com.sample.spring.entity.ClientDetailEntity;
import com.sample.spring.mapper.ClientDetailMapper;
import com.sample.spring.repository.ClientDetailRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientDetailsServiceImpl implements ClientDetailsService {
    private final ClientDetailRepository repository;

    @Override
    public BaseClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetailEntity client = this.repository.findClientByClientId(clientId);
        if (ObjectUtils.isEmpty(client))
            throw new ClientRegistrationException("Client id " + clientId + " not found.");
        return ClientDetailMapper.INSTANCE.entityToDto(client);
    }
}
