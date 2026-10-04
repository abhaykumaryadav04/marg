package com.forensiq.marg.assessment.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forensiq.marg.assessment.entity.AssessmentResponse;

@Repository 
public interface AssessmentResponseRepo extends JpaRepository<AssessmentResponse,UUID>{
List<AssessmentResponse> findByAssessmentId(UUID assessmentId);
}
