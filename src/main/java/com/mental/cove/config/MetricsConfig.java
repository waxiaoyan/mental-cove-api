package com.mental.cove.config;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class MetricsConfig {
    @Bean
    public MeterRegistry customizeMeterRegistry(MeterRegistry registry) {
        registry.config().meterFilter(io.micrometer.core.instrument.MeterFilter.deny(id -> id.getName().startsWith("system.cpu")));
        registry.config().meterFilter(io.micrometer.core.instrument.MeterFilter.deny(id -> id.getName().startsWith("process.cpu")));
        return registry;
    }
}