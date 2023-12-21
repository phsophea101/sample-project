package com.sample.spring.security.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.util.Map;

@JsonSerialize(using = OAuth2ExceptionResponseJackson2Serializer.class)
public class OAuth2ExceptionResponse extends OAuth2Exception {

    private final Map<String, Object> response;

    public OAuth2ExceptionResponse(Map<String, Object> response) {
        super(String.valueOf(response.get("result_message")));
        this.response = response;
    }

    public Map<String, Object> getResponse() {
        return response;
    }
}
