package com.mental.cove.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mental.cove.data.WechatSessionResponse;
import feign.codec.Decoder;
import feign.codec.StringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    private final ObjectMapper objectMapper;

    public FeignConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public Decoder feignDecoder() {
        return (response, type) -> {
            if (type == WechatSessionResponse.class) {
                String body = (String) new StringDecoder().decode(response, String.class);
                return objectMapper.readValue(body, WechatSessionResponse.class);
            }
            return new StringDecoder().decode(response, type);
        };
    }
}
