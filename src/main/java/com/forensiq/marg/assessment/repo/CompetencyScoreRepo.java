package com.forensiq.marg.assessment.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forensiq.marg.assessment.entity.CompetencyScore;


@Repository 
public interface CompetencyScoreRepo extends  JpaRepository<CompetencyScore,UUID>{
List<CompetencyScore> findByAssessmentId(UUID assessmentId);

Optional<CompetencyScore> findByAssessmentIdAndSkill(UUID assessmentId, String skill);

}
