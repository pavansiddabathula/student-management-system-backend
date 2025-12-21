package com.techcode.studentmgmt.dto.requestdto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.techcode.studentmgmt.enums.TrainingLevel;
import com.techcode.studentmgmt.enums.TrainingMode;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainingRequest {

    @NotBlank(message = "Training name must not be blank")
    @Size(min = 3, max = 150, message = "Training name must be between 3 and 150 characters")
    private String trainingName;

    @NotBlank(message = "Description must not be blank")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 week")
    private Integer durationInWeeks;

    @NotNull(message = "Training mode is required")
    private TrainingMode mode;

    @NotNull(message = "Training level is required")
    private TrainingLevel level;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or a future date")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be a future date")
    private LocalDate endDate;

    @NotBlank(message = "Session timing must not be blank")
    @Size(max = 50, message = "Session timing must not exceed 50 characters")
    private String sessionTiming;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer totalSeats;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be zero or a positive value")
    private BigDecimal price;

    @NotBlank(message = "Trainer name must not be blank")
    @Size(max = 100, message = "Trainer name must not exceed 100 characters")
    private String trainerName;

    @NotBlank(message = "Syllabus PDF URL must not be blank")
    @Pattern(
        regexp = "^(http|https)://.*$",
        message = "Syllabus PDF URL must be a valid HTTP or HTTPS URL"
    )
    private String syllabusPdfUrl;
}
