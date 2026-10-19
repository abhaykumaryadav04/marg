package com.forensiq.marg.folowups.entity;


import java.math.BigDecimal;

import java.time.LocalDateTime;

import java.util.Set;
import java.util.UUID;

import com.forensiq.marg.folowups.enumeration.FollowUpsStatus;
import com.forensiq.marg.user.entity.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@Data 
public class FollowUp {
    @Id 
    @GeneratedValue 
    private UUID followUpId;
    @ManyToOne (fetch = FetchType.LAZY,optional = false)
    @JoinColumn (name = "traineeId")
    private User trainee;
    @Column(nullable = false, length = 200)
    private String trainingName;
    @Column(nullable = false) 
    private LocalDateTime compeletedAt;
     @Column(nullable = false)
     private Integer intervalDates;
     @Column (nullable = false)
     private LocalDateTime dueDate;
     @Column (nullable = false)
     private FollowUpsStatus status;
      @Column(length = 30)
    private String employmentStatus;

    @Column(length = 150)
    private String currentJobRole;

    @Column(length = 30)
    private String employmentType;

    private LocalDateTime employmentStartDate;
    //when consent provides
     @Column(precision = 12, scale = 2)
    private BigDecimal monthlySalary;

    @Column(length = 10)
    private String salaryCurrency ;

    @Column(length = 30)
    private String jobRelevance;

    @ElementCollection
    @CollectionTable(
        name = "follow_up_skills_applied",
        joinColumns = @JoinColumn(name = "follow_up_id")
    )
    @Column(name = "skill_name", nullable = false, length = 100)
    private Set<String> skillsApplied;

    @Column(length = 1000)
    private String additionalSkillGaps;
    private Integer trainingHelpfulnessRating;

    @Column(length = 2000)
    private String notes;

    @Column(nullable = false)
    private Boolean consentToUseInAggregatedAnalytics ;
     private LocalDateTime respondedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    private LocalDateTime verifiedAt;

    @Column(length = 1000)
    private String verificationNotes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
