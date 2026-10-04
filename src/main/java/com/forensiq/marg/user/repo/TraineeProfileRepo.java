package com.forensiq.marg.user.repo;

import java.util.Optional;
import java.util.UUID;
import  com.forensiq.marg.user.entity.TraineeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeProfileRepo extends JpaRepository<TraineeProfile,UUID> {
Optional<TraineeProfile> findByUserId(UUID userId);
}
