package com.chromaEmbeddingChroma.vectorEmbedding.service;

import com.chromaEmbeddingChroma.vectorEmbedding.config.ChromadbConfig;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import com.chromaEmbeddingChroma.vectorEmbedding.dto.OllamaRequest;
import com.chromaEmbeddingChroma.vectorEmbedding.dto.OllamaResponse;
import org.springframework.ai.chroma.ChromaApi;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.microsoft.cognitiveservices.speech.*;

import java.util.*;

@Service
public class ChromaDBService {

    private final RestTemplate restTemplate;
    private final ChromaApi chromaClient;

    private final OllamaService ollamaService;

    public ChromaDBService(ChromaApi chromaClient,OllamaService ollamaService) {
        this.restTemplate = new RestTemplate();
        this.chromaClient= chromaClient;
        this.ollamaService=ollamaService;
    }

    public String addDocuments(String cid,String[] documents, String[] ids) {

        List<Map<String, Object>> requestBody = new ArrayList<>();



        List<float[]> embeddingRequest=new ArrayList<>();
        for (int i=0 ;i<documents.length;i++){
            EmbeddingResponse res=ollamaService.getEmbeddingResponse(documents[i]);
            float[] embd=res.getResult().getOutput();
            embeddingRequest.add(embd);

            Map<String, Object> metadata1=new HashMap<>();
            metadata1.put("documents", documents[i]);
            metadata1.put("ids", ids[i]);
            requestBody.add(metadata1);
        }

        ChromaApi.AddEmbeddingsRequest adEmbd=new ChromaApi
                .AddEmbeddingsRequest(List.of(ids),embeddingRequest, requestBody,List.of(documents));
        chromaClient.upsertEmbeddings(cid,adEmbd);

         return "success";
    }

    public String createCollection(String documents, String ids) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("documents", documents);
        requestBody.put("ids", ids);
        ChromaApi.CreateCollectionRequest req= new ChromaApi.CreateCollectionRequest("spring",requestBody);

        ChromaApi.Collection collection=chromaClient.createCollection(req);

        return collection.id();
    }
    public String queryDocuments(String query, String id) {


        EmbeddingResponse res=ollamaService.getEmbeddingResponse(query);
        float[] embd=res.getResult().getOutput();
        System.out.println(embd);
        ChromaApi.QueryRequest qr=new ChromaApi.QueryRequest(embd,5);
        ChromaApi.QueryResponse response=chromaClient.queryCollection(id,qr);
        String prompt=createPrompt(query,response.documents().get(0));
        callLlama3(prompt);
        return response.documents().toString();
    }
    private static String createPrompt(String query,List<String> documents){

        StringBuilder prompt = new StringBuilder();
        prompt.append("The user asked: '").append(query).append("'\n\n");
        prompt.append("Here are some relevant documents:\n");

        for (int i = 0; i < documents.size(); i++) {
            prompt.append("Document ").append(i + 1).append(": ").append(documents.get(i)).append("\n");
        }

        prompt.append("\nGiven this information, please provide a detailed response to the user's query.");


        String finalPrompt = prompt.toString();
        System.out.println("Generated Prompt:\n" + finalPrompt);
        return finalPrompt;
    }
    private static void callLlama3(String prompt){

        OllamaApi ollamaApi = new OllamaApi();
        List<Message> messages=new ArrayList<>();
        Message sysMessage= new Message(Message.Role.SYSTEM,"You're an AI assistant that helps people find information. " +
                "Answers shouldn't be longer than 20 words because you are on a phone. " +
                "You could use 'um' or 'let me see' to make it more natural and add some disfluency.",null,null);

        Message userMessage= new Message(Message.Role.USER,prompt,null,null);
        messages.add(sysMessage);
        messages.add(userMessage);

        // Create OllamaOptions for the request
        OllamaOptions ollamaOptions = new OllamaOptions();
        ollamaOptions.setTemperature(0.7f);
        ollamaOptions.setMaxTokens(100);

        Map<String,Object> option=new HashMap<>();
        option.put("Options",ollamaOptions);
        // Create a ChatRequest object
        OllamaApi.ChatRequest chatRequest = new OllamaApi.ChatRequest(
                "llama3", messages, false, "json", "5m", null, option);
        OllamaApi.ChatResponse res=ollamaApi.chat(chatRequest);
        // Print the chat request
        System.out.println("ChatRequest created: " + res.message().content());
    }
}
