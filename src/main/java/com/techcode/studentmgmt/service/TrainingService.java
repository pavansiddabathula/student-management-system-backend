package com.techcode.studentmgmt.service;

import org.springframework.http.ResponseEntity;

import com.techcode.studentmgmt.dto.requestdto.CreateTrainingRequest;
import com.techcode.studentmgmt.enums.TrainingMode;

public interface TrainingService {

    ResponseEntity<?> createTraining(CreateTrainingRequest request);

    ResponseEntity<?> updateTraining(String trainingCode, CreateTrainingRequest request);

    ResponseEntity<?> getAllTrainings();

    ResponseEntity<?> getTrainingByCode(String trainingCode);

    ResponseEntity<?> getTrainingsByMode(TrainingMode mode);
    
    ResponseEntity<?> registerTraining(String trainingId);
    
    ResponseEntity<?> getRegistrationsByTraining(String trainingId);

	ResponseEntity<?> getRegistrationsByRollNumber(String rollNumber);

	ResponseEntity<?> getRegistrationByTrainingAndStudent(String trainingCode, String rollNumber);

	ResponseEntity<?> initiatePayment(String trainingCode);
}
