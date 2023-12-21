package com.sample.spring.service.impl;

import com.sample.spring.dto.ClientDetailDto;
import com.sample.spring.entity.ClientDetailEntity;
import com.sample.spring.mapper.ClientDetailMapper;
import com.sample.spring.repository.ClientDetailRepository;
import com.sample.spring.service.ClientDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientDetailServiceImpl implements ClientDetailService {
    private final ClientDetailRepository repository;

    @Override
    public ClientDetailDto findClientByClientId(String clientId) {
        ClientDetailEntity entity = this.repository.findClientByClientId(clientId);
        return ClientDetailMapper.INSTANCE.to(entity);
    }
}
