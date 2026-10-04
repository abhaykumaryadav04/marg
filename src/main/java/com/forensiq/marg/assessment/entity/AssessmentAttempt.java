package com.forensiq.marg.assessment.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.forensiq.marg.user.entity.User;

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
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@Data 
public class AssessmentAttempt {
@Id 
@GeneratedValue 
private UUID id;
 @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
private User user;
@Column(nullable = false)
private String status;
@Column(nullable = false)
private LocalDateTime startedAt;
private LocalDateTime completedAt;
}
