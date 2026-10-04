package com.forensiq.marg.assessment.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forensiq.marg.assessment.entity.AssessmentAttempt;

@Repository 
public interface AssessmentAttemptRepo extends  JpaRepository<AssessmentAttempt,UUID>{
Optional<AssessmentAttempt> findByIdAndUserId(UUID id,UUID userId);
}
