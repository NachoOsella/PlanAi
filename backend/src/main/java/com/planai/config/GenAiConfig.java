package com.planai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;
import com.google.genai.Client;

/**
 * GenAiConfig
 */
@Configuration
public class GenAiConfig {

    @Value("${genai.api-key}")
    private String apiKey;

    @Bean
    public Client genAiClient() {
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }
}
