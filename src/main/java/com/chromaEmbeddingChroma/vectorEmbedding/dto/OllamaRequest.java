package com.chromaEmbeddingChroma.vectorEmbedding.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OllamaRequest {


    private String model;
    private String prompt;

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    private Boolean stream;

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
