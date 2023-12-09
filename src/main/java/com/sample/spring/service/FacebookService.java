package com.sample.spring.service;

import com.sample.spring.dto.FacebookRequestDto;
import facebook4j.FacebookException;

public interface FacebookService {
    void getPost(FacebookRequestDto dto) throws FacebookException;
}
