package com.Ai_Text_Summarizer.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "text_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_text" , columnDefinition = "TEXT" , nullable = false)
    private String originalText;

    @Column(name = "summarized_text" , columnDefinition = "TEXT")
    private String summarizedText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "model_used")
    private String modelUsed;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }



}
