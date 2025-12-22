package com.techcode.studentmgmt.dto.responsedto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techcode.studentmgmt.enums.TrainingLevel;
import com.techcode.studentmgmt.enums.TrainingMode;
import com.techcode.studentmgmt.enums.TrainingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingResponse {

    private String trainingCode;
    private String trainingName;
    private String description;
    private Integer durationInWeeks;
    private TrainingMode mode;
    private TrainingLevel level;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String sessionTiming;
    private Integer totalSeats;
    private BigDecimal price;
    private String trainerName;
    private String syllabusPdfUrl;
    private TrainingStatus status;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
