package com.chromaEmbeddingChroma.vectorEmbedding.dto;


import lombok.Data;

@Data
public class DocumentRequest {
    private String[] documents;
    private String[] ids;

    public String[] getDocuments() {
        return documents;
    }

    public void setDocuments(String[] documents) {
        this.documents = documents;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String[] getIds() {
        return ids;
    }
// getters and setters
}
