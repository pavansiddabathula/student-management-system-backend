package com.techcode.studentmgmt.modelmappers;

import com.techcode.studentmgmt.dto.responsedto.ActionLinks;
import com.techcode.studentmgmt.dto.responsedto.TrainingRegistrationResponse;
import com.techcode.studentmgmt.entity.TrainingRegistration;

public class TrainingRegistrationMapper {

    private static final String API_BASE_URL = "http://localhost:8080";
    private static final String UI_BASE_URL  = "http://localhost:8080";

    private TrainingRegistrationMapper() {}

    // Generic response mapper (used everywhere)
    public static TrainingRegistrationResponse toResponse(
            TrainingRegistration registration,
            boolean paymentRequired) {

        ActionLinks links = ActionLinks.builder()
                .payNowUrl(paymentRequired
                        ? API_BASE_URL + "/api/trainings/"
                          + registration.getTrainingCode() + "/pay"
                        : null)
                .skipPaymentUrl(UI_BASE_URL + "/home")
                .build();

        return TrainingRegistrationResponse.builder()
                .trainingCode(registration.getTrainingCode())
                .trainingName(registration.getTrainingName())
                .studentRollNumber(registration.getRollNumber())
                .studentName(registration.getStudentName())
                .registrationStatus(registration.getRegistrationStatus())
                .paid(registration.getPaid())
                .amountPaid(registration.getAmountPaid())
                .paymentRequired(paymentRequired)
                .registeredAt(registration.getRegisteredAt())
                .actionLinks(links)
                .build();
    }
}
