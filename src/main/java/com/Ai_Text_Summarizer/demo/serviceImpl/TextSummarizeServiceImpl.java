package com.Ai_Text_Summarizer.demo.serviceImpl;

import com.Ai_Text_Summarizer.demo.DTO.SummarizeRequest;
import com.Ai_Text_Summarizer.demo.DTO.SummarizeResponse;
import com.Ai_Text_Summarizer.demo.dao.TextSummaryRepository;
import com.Ai_Text_Summarizer.demo.entity.*;
import com.Ai_Text_Summarizer.demo.service.TextSummarizeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class TextSummarizeServiceImpl implements TextSummarizeService {

    private static final Logger logger = LoggerFactory.getLogger(TextSummarizeServiceImpl.class);

    private final TextSummaryRepository repository;
    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.model}")
    private String geminiModel;

    public TextSummarizeServiceImpl(TextSummaryRepository repository) {
        this.repository = repository;
        this.webClient = WebClient.create("https://generativelanguage.googleapis.com");
    }

    @Override
    public SummarizeResponse summarizeText(SummarizeRequest request) {

        validateRequest(request);

        String prompt = buildPrompt(request.getText());

        String summary = callGeminiApi(prompt);

        TextSummary entity = new TextSummary();
        entity.setOriginalText(request.getText());
        entity.setSummarizedText(summary);
        entity.setModelUsed(geminiModel);

        TextSummary saved = repository.save(entity);

        return new SummarizeResponse(
                saved.getId(),
                saved.getOriginalText(),
                saved.getSummarizedText(),
                saved.getModelUsed()
        );
    }

    @Override
    public void deleteAllSummaries() {
        repository.deleteAll();
        logger.info("All summaries deleted");

    }

    @Override
    public List<SummarizeResponse> getAllSummaries() {
        return repository.findAll()
                .stream()
                .map(e -> new SummarizeResponse(
                        e.getId(),
                        e.getOriginalText(),
                        e.getSummarizedText(),
                        e.getModelUsed()
                ))
                .toList();
    }

    private void validateRequest(SummarizeRequest request) {
        if (request == null || request.getText() == null || request.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
    }

    private String buildPrompt(String text) {
        return "Summarize the following text briefly:\n\n" + text;
    }

    private String callGeminiApi(String prompt) {

        try {
            String url = "/v1beta/models/" + geminiModel + ":generateContent?key=" + geminiApiKey;

            Map<String, Object> requestBody = Map.of(
                    "contents", new Object[]{
                            Map.of("parts", new Object[]{
                                    Map.of("text", prompt)
                            })
                    }
            );

            Map response = webClient.post()
                    .uri(url)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            logger.info("Gemini API raw response: {}", response);

            if (response == null || !response.containsKey("candidates")) {
                throw new RuntimeException("Invalid response from Gemini API");
            }

            Map candidate = (Map) ((List) response.get("candidates")).get(0);
            Map content = (Map) candidate.get("content");
            List parts = (List) content.get("parts");
            Map part = (Map) parts.get(0);

            return part.get("text").toString();

        } catch (Exception e) {
            logger.error("Error calling Gemini API", e);
            throw new RuntimeException("Gemini API call failed: " + e.getMessage());
        }
    }



}
