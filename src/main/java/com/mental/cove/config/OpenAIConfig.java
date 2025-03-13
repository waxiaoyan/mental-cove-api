package com.mental.cove.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class OpenAIConfig {

//    @Value("${openai.api.key}")
//    private String openaiApiKey;

    @Value("${deepseek.api.key}")
    private String deepseekApiKey;

//    @Bean
//    @Primary
//    public RestTemplate openAIRestTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getInterceptors().add(((request, body, execution) -> {
//            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
//            return execution.execute(request, body);
//        }));
//        return restTemplate;
//    }

    @Bean
    public RestTemplate deepSeekRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + deepseekApiKey);
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            return execution.execute(request, body);
        }));
        return restTemplate;
    }
}
