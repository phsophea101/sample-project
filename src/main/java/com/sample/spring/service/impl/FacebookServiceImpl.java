package com.sample.spring.service.impl;

import com.sample.spring.dto.FacebookRequestDto;
import com.sample.spring.service.FacebookService;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Reading;
import org.springframework.stereotype.Service;

@Service
public class FacebookServiceImpl implements FacebookService {
    @Override
    public void getPost(FacebookRequestDto dto) throws FacebookException {
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.getFeed(dto.getId(), new Reading().limit(10));
    }
}
