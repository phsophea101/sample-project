package com.sample.spring.security.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class OAuth2ExceptionResponseJackson2Serializer extends StdSerializer<OAuth2ExceptionResponse> {

    public OAuth2ExceptionResponseJackson2Serializer() {
        super(OAuth2ExceptionResponse.class);
    }

    @Override
    public void serialize(OAuth2ExceptionResponse ex, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        provider.defaultSerializeValue(ex.getResponse(), generator);
    }

}
