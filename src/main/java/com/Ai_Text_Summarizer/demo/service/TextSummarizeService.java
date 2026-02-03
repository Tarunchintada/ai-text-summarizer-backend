package com.Ai_Text_Summarizer.demo.service;

import com.Ai_Text_Summarizer.demo.DTO.SummarizeRequest;
import com.Ai_Text_Summarizer.demo.DTO.SummarizeResponse;

import java.util.List;

public interface TextSummarizeService {

    SummarizeResponse summarizeText(SummarizeRequest request);

    void deleteAllSummaries();


    List<SummarizeResponse> getAllSummaries();
}
