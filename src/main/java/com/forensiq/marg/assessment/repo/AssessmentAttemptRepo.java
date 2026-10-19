package com.forensiq.marg.assessment.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.forensiq.marg.assessment.entity.AssessmentAttempt;


public interface AssessmentAttemptRepo extends  JpaRepository<AssessmentAttempt,UUID>{
Optional<AssessmentAttempt> findByIdAndUserId(UUID id,UUID userId);
Optional<AssessmentAttempt> findFirstByUser_IdOrderByStartedAtDesc(UUID userId);

}