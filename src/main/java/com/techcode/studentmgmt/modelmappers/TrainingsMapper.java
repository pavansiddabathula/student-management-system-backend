package com.techcode.studentmgmt.modelmappers;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import com.techcode.studentmgmt.dto.requestdto.CreateTrainingRequest;
import com.techcode.studentmgmt.dto.responsedto.TrainingResponse;
import com.techcode.studentmgmt.entity.Training;
import com.techcode.studentmgmt.enums.TrainingStatus;

public class TrainingsMapper {

    private TrainingsMapper() {
        // Prevent instantiation
    }
    
    public static String generateTrainingCode() {
	    int random = ThreadLocalRandom.current().nextInt(100000, 1000000);
	    return String.valueOf(random);
	}

    public static Training toEntity(CreateTrainingRequest request, String createdBy) {
    	
    	


        return Training.builder()
        		.trainingCode(generateTrainingCode().toString())
                .trainingName(request.getTrainingName())
                .description(request.getDescription())
                .durationInWeeks(request.getDurationInWeeks())
                .mode(request.getMode())
                .level(request.getLevel())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .sessionTiming(request.getSessionTiming())
                .totalSeats(request.getTotalSeats())
                .price(request.getPrice())
                .trainerName(request.getTrainerName())
                .syllabusPdfUrl(request.getSyllabusPdfUrl())
                .status(TrainingStatus.DRAFT)
                .createdBy(createdBy)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static TrainingResponse toResponse(Training training) {

        return TrainingResponse.builder()
                .trainingCode(training.getTrainingCode())
                .trainingName(training.getTrainingName())
                .description(training.getDescription())
                .durationInWeeks(training.getDurationInWeeks())
                .mode(training.getMode())
                .level(training.getLevel())
                .startDate(training.getStartDate())
                .endDate(training.getEndDate())
                .sessionTiming(training.getSessionTiming())
                .totalSeats(training.getTotalSeats())
                .price(training.getPrice())
                .trainerName(training.getTrainerName())
                .syllabusPdfUrl(training.getSyllabusPdfUrl())
                .status(training.getStatus())
                .createdBy(training.getCreatedBy())
                .createdAt(training.getCreatedAt())
                .build();
    }
}
