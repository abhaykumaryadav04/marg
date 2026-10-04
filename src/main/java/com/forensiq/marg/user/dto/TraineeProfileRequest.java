package com.forensiq.marg.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record TraineeProfileRequest(
     @Size(max = 20)
        String phone,

        String dateOfBirth,

        String gender,

        String district,

        String state,

        String qualification,

        String specialization,

        @Min(0)
        Integer experienceYears,

        String employmentStatus,

        String currentJobRole,

        String targetJobRole
) {

}
