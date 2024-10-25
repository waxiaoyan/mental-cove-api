package com.mental.cove.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api.rate-limit")
@Data
public class  RateLimitProperties {
    private int maxRequests;
    private long duration;

}
