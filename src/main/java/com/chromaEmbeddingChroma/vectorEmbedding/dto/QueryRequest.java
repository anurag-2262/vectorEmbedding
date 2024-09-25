package com.chromaEmbeddingChroma.vectorEmbedding.dto;

import lombok.Data;

@Data
public class QueryRequest {
    private String query;
    private String nResults;

    // getters and setters

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getnResults() {
        return nResults;
    }

    public void setnResults(String nResults) {
        this.nResults = nResults;
    }
}