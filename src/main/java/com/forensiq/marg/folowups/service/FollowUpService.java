package com.forensiq.marg.folowups.service;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.forensiq.marg.folowups.dto.FollowUpRequest;
import com.forensiq.marg.folowups.dto.FollowUpRequestResponse;
import com.forensiq.marg.folowups.dto.FollowUpResponse;
import com.forensiq.marg.folowups.entity.FollowUp;
import com.forensiq.marg.folowups.enumeration.FollowUpsStatus;
import com.forensiq.marg.folowups.repo.FollowUpsRepo;
import com.forensiq.marg.user.ennumeration.Role;
import com.forensiq.marg.user.entity.User;
import com.forensiq.marg.user.repo.UserRepo;



@Service 
public class FollowUpService {
    private final FollowUpsRepo followUpsRepo;
    private final UserRepo userRepo;
    FollowUpService(FollowUpsRepo followUpsRepo,UserRepo userRepo) {
        this.followUpsRepo = followUpsRepo;
        this.userRepo=userRepo;
    }
       private static final Set<Integer> SUPPORTED_INTERVALS =Set.of(30, 90, 180);
        private FollowUp getOwnedFollowUp( UUID followUpId, UUID traineeId) {

        return followUpsRepo.findByIdAndTraineeId( followUpId,traineeId ).orElseThrow(() ->
                        new ResponseStatusException( HttpStatus.NOT_FOUND,"Follow-up not found" ) );
    }
      private FollowUpResponse mapToResponse( FollowUp followUp) {

        return new FollowUpResponse(
                followUp.getFollowUpId(),
                followUp.getTrainingName(),
                followUp.getCompeletedAt(),
                followUp.getIntervalDates(),
                followUp.getDueDate(),
                followUp.getStatus(),
                followUp.getEmploymentStatus(),
                followUp.getCurrentJobRole(),
                followUp.getEmploymentType(),

                // Entity uses LocalDate; your DTO uses LocalDateTime.
                followUp.getEmploymentStartDate() == null
                        ? null
                        : followUp.getEmploymentStartDate(),

                followUp.getMonthlySalary(),
                followUp.getSalaryCurrency(),
                followUp.getJobRelevance(),

                followUp.getSkillsApplied() == null
                        ? Set.of()
                        : Set.copyOf(followUp.getSkillsApplied()),

                followUp.getAdditionalSkillGaps(),
                followUp.getTrainingHelpfulnessRating(),
                followUp.getNotes(),
                followUp.getRespondedAt(),
                followUp.getVerifiedAt(),
                followUp.getCreatedAt(),
                followUp.getUpdatedAt()
        );
    }
     private User getTrainee(UUID traineeId) {

        User trainee = userRepo.findById(traineeId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Trainee not found"
                        )
                );

        if (trainee.getRole() != Role.TRAINEE) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This operation is available to trainees only"
            );
        }

        return trainee;
    }
    @Transactional 
   public  FollowUpResponse scheduleFollowUp(UUID traineeId,FollowUpRequest request){
   User trainee=getTrainee(traineeId);
   String trainingName=request.trainingName();
   Integer intervalDays=request.intervalDays();
   if(SUPPORTED_INTERVALS.contains(intervalDays)){
     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Follow-up interval must be 30, 90 or 180 days");
   }
    boolean alreadyExists = followUpsRepo.existsByTraineeIdAndTrainingNameIgnoreCaseAndIntervalDays( traineeId, trainingName, intervalDays );
    if(alreadyExists){
           throw new ResponseStatusException( HttpStatus.CONFLICT,"This follow-up has already been scheduled");
    }
     LocalDateTime completedAt = request.trainingCompletedAt();
     LocalDateTime dueDate =completedAt.plusDays(intervalDays);

    FollowUp followUp=FollowUp.builder().trainee(trainee)
                                        .trainingName(trainingName)
                                        .compeletedAt(request.trainingCompletedAt())
                                        .intervalDates(intervalDays)
                                        .dueDate(dueDate)
                                        .status(FollowUpsStatus.SCHEDULED)
                                        .consentToUseInAggregatedAnalytics(false)
                                        .skillsApplied(new HashSet<>())
                                        .build();
       followUpsRepo.save(followUp);
        return mapToResponse(followUp);                                  
   }
   @Transactional (readOnly =  true)
   public List<FollowUpResponse> getFollowUpResponse(UUID traineeId){
      getTrainee(traineeId);
     return followUpsRepo.findByTraineeIdOrderByDueDateAsc(traineeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
   }
   @Transactional (readOnly = true)
   public  FollowUpResponse submit(UUID traineeId,UUID followUpId,FollowUpRequestResponse request){
      getTrainee(traineeId);
     FollowUp followUp = getOwnedFollowUp(followUpId,traineeId );
      if (followUp.getStatus() == FollowUpsStatus.VERIFIED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A verified follow-up cannot be modified"
            );
        }
         if (followUp.getStatus() == FollowUpsStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This follow-up has been cancelled"
            );
        }

        if (followUp.getStatus() != FollowUpsStatus.SCHEDULED
                && followUp.getStatus() != FollowUpsStatus.REJECTED) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This follow-up cannot accept another response"
            );
        }

        if (followUp.getDueDate().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This follow-up is not due yet"
            );
        }
        
        followUp.setEmploymentStatus(
                request.employmentStatus()
        );

        followUp.setCurrentJobRole(
                request.currentJobRole()
        );

        followUp.setEmploymentType(
                request.employmentType()
        );

        followUp.setEmploymentStartDate(
                request.employmentStartDate()
        );

        followUp.setMonthlySalary(
                request.monthlySalary()
        );

        String currency = request.salaryCurrency();

        followUp.setSalaryCurrency(
                currency == null || currency.isBlank()
                        ? "INR"
                        : currency.strip().toUpperCase(Locale.ROOT)
        );

        followUp.setJobRelevance(
                request.jobRelevance()
        );

        followUp.setSkillsApplied(
                request.skillsApplied() == null
                        ? new HashSet<>()
                        : new HashSet<>(request.skillsApplied())
        );

        followUp.setAdditionalSkillGaps(
                request.additionalSkillGaps()
        );

        followUp.setTrainingHelpfulnessRating(
                request.trainingHelpfulnessRating()
        );

        followUp.setNotes(request.notes());

        followUp.setConsentToUseInAggregatedAnalytics(
                Boolean.TRUE.equals(
                        request.consentToUseInAggregatedAnalytics()
                )
        );

        followUp.setRespondedAt(LocalDateTime.now());

        // Submission does not mean verification.
        followUp.setVerifiedBy(null);
        followUp.setVerifiedAt(null);
        followUp.setVerificationNotes(null);

        followUp.setStatus(FollowUpsStatus.SUBMITTED);

        FollowUp saved = followUpsRepo.save(followUp);

        return mapToResponse(saved);
   }


}
