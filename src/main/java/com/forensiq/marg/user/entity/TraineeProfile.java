package com.forensiq.marg.user.entity;

import java.util.UUID;

import jakarta.persistence.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class TraineeProfile {
@Id 
@GeneratedValue 
private UUID id;
@OneToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
)
private User user;
private String phone;
private String dob;
private String gender;
private String district;
private String state;
private String qualification;
private String specialization;
private Integer experienceYears;
private String employmentStatus;
private String currentJobRole;
private String targetJobRole;

}
