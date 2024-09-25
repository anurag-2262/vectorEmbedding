package com.chromaEmbeddingChroma.vectorEmbedding.controller;

import com.chromaEmbeddingChroma.vectorEmbedding.dto.DocumentRequest;
import com.chromaEmbeddingChroma.vectorEmbedding.dto.QueryRequest;
import com.chromaEmbeddingChroma.vectorEmbedding.service.ChromaDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chromadb")
public class ChromaDBController {

    private final ChromaDBService chromaDBService;

    public ChromaDBController(ChromaDBService chromaDBService) {
        this.chromaDBService = chromaDBService;
    }

    @PostMapping("/add")
    public String addDocuments(@RequestBody DocumentRequest request) {
        String cid="6df18125-7230-4e0f-8a5c-b8cbe32f2694";
        String[] dr= request.getDocuments();
        String[] ir=request.getIds();
        return chromaDBService.addDocuments(cid,dr,ir);
    }
    @PostMapping("/create")
    public String createCollection(@RequestBody DocumentRequest request) {
        String[] dr= request.getDocuments();
        String[] ir=request.getIds();
        return chromaDBService.createCollection(dr[0],ir[0]);
    }

    @PostMapping("/query")
    public String queryDocuments(@RequestBody QueryRequest request) {
        String qr=request.getQuery();
        String qn=request.getnResults();
        return chromaDBService.queryDocuments(qr,qn);
    }
}

