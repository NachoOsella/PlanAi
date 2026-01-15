package com.planai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AI Configuration for Spring AI with Groq compatibility.
 * 
 * This configuration creates custom OpenAiApi and OpenAiChatModel beans that properly
 * handle Groq's OpenAI-compatible API. Groq rejects the 'extra_body' property that
 * Spring AI's OpenAiApi includes by default, so we configure the RestClient to exclude
 * null/empty fields from serialization.
 */
@Configuration
public class AiConfig {

    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    @Value("${spring.ai.openai.chat.options.temperature:0.7}")
    private Float temperature;

    /**
     * Creates a custom OpenAiApi configured for Groq compatibility.
     * 
     * The key fix is configuring the ObjectMapper to not serialize null values,
     * which prevents the empty 'extra_body' field from being sent to Groq.
     */
    @Bean
    public OpenAiApi openAiApi() {
        // Create ObjectMapper that excludes null values and ignores unknown properties
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.findAndRegisterModules();

        // Build RestClient with custom message converter
        RestClient.Builder restClientBuilder = RestClient.builder();
        restClientBuilder.messageConverters(converters -> {
            converters.removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
            converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        });

        // Create OpenAiApi with custom RestClient
        WebClient.Builder webClientBuilder = WebClient.builder();
        return new OpenAiApi(baseUrl, apiKey, restClientBuilder, webClientBuilder);
    }

    /**
     * Creates a custom OpenAiChatModel that uses our Groq-compatible OpenAiApi.
     * This overrides Spring AI's auto-configured model.
     */
    @Bean
    public OpenAiChatModel openAiChatModel(OpenAiApi openAiApi) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .withModel(model)
                .withTemperature(temperature == null ? null : temperature.doubleValue())
                .build();

        return new OpenAiChatModel(openAiApi, options);
    }

    /**
     * Creates the ChatClient using our custom OpenAiChatModel.
     */
    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}
