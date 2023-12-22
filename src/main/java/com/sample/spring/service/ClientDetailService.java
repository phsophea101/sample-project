package com.sample.spring.service;

import com.sample.spring.dto.ClientDetailDto;

public interface ClientDetailService {
    ClientDetailDto findClientByClientId(String clientId);
}
