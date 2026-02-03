package com.Ai_Text_Summarizer.demo.controller;


import com.Ai_Text_Summarizer.demo.DTO.SummarizeRequest;
import com.Ai_Text_Summarizer.demo.DTO.SummarizeResponse;
import com.Ai_Text_Summarizer.demo.service.TextSummarizeService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/summarize")
public class TextSummarizeController {

    private final TextSummarizeService service;

    public TextSummarizeController(TextSummarizeService service) {
        this.service = service;
    }

    @PostMapping
    public SummarizeResponse summarize(@RequestBody SummarizeRequest request) {
        return service.summarizeText(request);
    }

    @GetMapping("/history")
    public List<SummarizeResponse> getHistory(){
    return service.getAllSummaries();
    }

    @DeleteMapping("/history")
    public void deleteHistory(){
        service.deleteAllSummaries();
    }
}


