package com.Ai_Text_Summarizer.demo.dao;

import com.Ai_Text_Summarizer.demo.entity.TextSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextSummaryRepository extends JpaRepository<TextSummary,Long> {
}
