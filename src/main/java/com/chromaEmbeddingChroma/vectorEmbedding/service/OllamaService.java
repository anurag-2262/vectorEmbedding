package com.chromaEmbeddingChroma.vectorEmbedding.service;

import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OllamaService {

    public EmbeddingResponse getEmbeddingResponse(String query) {

        OllamaApi ollamaApi = new OllamaApi();

        OllamaEmbeddingModel embeddingModel = new OllamaEmbeddingModel(ollamaApi,
                OllamaOptions.builder()
                        .withModel(OllamaModel.MISTRAL.id())
                        .build());

        EmbeddingResponse embeddingResponse = embeddingModel.call(
                new EmbeddingRequest(List.of(query),
                        OllamaOptions.builder().withModel("chroma/all-minilm-l6-v2-f32").withTruncate(false).build()));
        return embeddingResponse;
    }

    public EmbeddingResponse getEmbeddingForDocuments(String[] documents) {

        OllamaApi ollamaApi = new OllamaApi();

        OllamaEmbeddingModel embeddingModel = new OllamaEmbeddingModel(ollamaApi,
                OllamaOptions.builder()
                        .withModel(OllamaModel.MISTRAL.id())
                        .build());

        EmbeddingResponse embeddingResponse = embeddingModel.call(
                new EmbeddingRequest(List.of(documents),
                        OllamaOptions.builder().withModel("chroma/all-minilm-l6-v2-f32").withTruncate(false).build()));
        return embeddingResponse;
    }

}
