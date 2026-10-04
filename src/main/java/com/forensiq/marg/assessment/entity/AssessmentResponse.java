package com.forensiq.marg.assessment.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class AssessmentResponse {
@Id 
@GeneratedValue 
private  UUID id;
@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assessment_id", nullable = false)
    private AssessmentAttempt assessment;

    @Column(nullable = false)
    private String questionId;

    @Column(nullable = false)
    private String skill;

    @Column(nullable = false)
    private boolean correct;
}
