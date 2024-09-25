package com.chromaEmbeddingChroma.vectorEmbedding.config;

import org.springframework.ai.chroma.ChromaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import org.springframework.web.client.RestClient;

@Configuration
public class ChromadbConfig {


    @Bean
    public ChromaApi chromaApi(RestClient.Builder restClientBuilder) {
        String chromaUrl = "http://localhost:8000";
        ChromaApi chromaApi = new ChromaApi(chromaUrl, RestClient.builder().requestFactory(new SimpleClientHttpRequestFactory()));
        return chromaApi;
    }
}
