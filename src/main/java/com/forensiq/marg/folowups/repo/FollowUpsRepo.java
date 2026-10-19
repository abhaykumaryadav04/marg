package com.forensiq.marg.folowups.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.forensiq.marg.folowups.entity.FollowUp;
import com.forensiq.marg.folowups.enumeration.FollowUpsStatus;


public interface FollowUpsRepo extends JpaRepository<FollowUp,UUID> {
    List<FollowUp> findByTraineeIdOrderByDueDateAsc(UUID traineeId);
    Optional<FollowUp> findByIdAndTraineeId(UUID followUpId,UUID userId);
      boolean existsByTraineeIdAndTrainingNameIgnoreCaseAndIntervalDays(
            UUID traineeId,
            String trainingName,
            Integer intervalDays
    );
    List<FollowUp> findAllByStatusAndDueDateLessThanEqualOrderByDueDateAsc(
            FollowUpsStatus status,
            LocalDate date
    );
    List<FollowUp> findAllByStatusOrderByDueDateAsc(
            FollowUpsStatus status
    );
}
