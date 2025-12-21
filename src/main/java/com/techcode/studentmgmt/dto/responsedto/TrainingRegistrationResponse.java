package com.techcode.studentmgmt.dto.responsedto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.techcode.studentmgmt.enums.RegistrationStatus;
import com.techcode.studentmgmt.enums.TrainingLevel;
import com.techcode.studentmgmt.enums.TrainingMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainingRegistrationResponse {
	
	// -------- Student Info --------
   private String studentRollNumber;
   private String studentName;

    // -------- Registration Info --------
    private RegistrationStatus registrationStatus;
    private Boolean paid;
    private BigDecimal amountPaid;
    private Boolean paymentRequired;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registeredAt;
    
    
    // -------- Training Info --------
    private Long trainingId;
    private String trainingCode;
    private String trainingName;
    private TrainingMode trainingMode;
    private TrainingLevel trainingLevel;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    //
    private ActionLinks actionLinks;

}

