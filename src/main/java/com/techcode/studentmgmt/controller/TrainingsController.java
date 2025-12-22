package com.techcode.studentmgmt.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techcode.studentmgmt.dto.requestdto.CreateTrainingRequest;
import com.techcode.studentmgmt.enums.TrainingMode;
import com.techcode.studentmgmt.service.TrainingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
@Slf4j
public class TrainingsController {

    private final TrainingService trainingService;

    // Create a new training (Admin)
    @PostMapping("/create")
    public ResponseEntity<?> createTraining(
            @RequestBody CreateTrainingRequest request) {

        log.info("HTTP POST /api/trainings/create | trainingName={}",
                request.getTrainingName());

        return trainingService.createTraining(request);
    }

    // Update existing training by code
    @PutMapping("/update/{trainingCode}")
    public ResponseEntity<?> updateTraining(
            @PathVariable String trainingCode,
            @Valid @RequestBody CreateTrainingRequest request) {

        log.info("HTTP PUT /api/trainings/update/{} | updating training",
                trainingCode);

        return trainingService.updateTraining(trainingCode, request);
    }

    // Fetch all trainings
    @GetMapping("/all")
    public ResponseEntity<?> getAllTrainings() {

        log.info("HTTP GET /api/trainings/all | fetch all trainings");

        return trainingService.getAllTrainings();
    }

    // Fetch training by training code
    @GetMapping("/code/{trainingCode}")
    public ResponseEntity<?> getTrainingByCode(
            @PathVariable String trainingCode) {

        log.info("HTTP GET /api/trainings/code/{} | fetch training",
                trainingCode);

        return trainingService.getTrainingByCode(trainingCode);
    }

    // Fetch all online trainings
    @GetMapping("/online")
    public ResponseEntity<?> getOnlineTrainings() {

        log.info("HTTP GET /api/trainings/online | fetch ONLINE trainings");

        return trainingService.getTrainingsByMode(TrainingMode.ONLINE);
    }

    // Fetch all offline trainings
    @GetMapping("/offline")
    public ResponseEntity<?> getOfflineTrainings() {

        log.info("HTTP GET /api/trainings/offline | fetch OFFLINE trainings");

        return trainingService.getTrainingsByMode(TrainingMode.OFFLINE);
    }

    // Register student for a training
    @PostMapping("/{trainingCode}/register")
    public ResponseEntity<?> registerTraining(
            @PathVariable String trainingCode) {

        log.info("HTTP POST /api/trainings/{}/register | register student",
                trainingCode);

        return trainingService.registerTraining(trainingCode);
    }

 // Admin: get all registrations for a training
    @GetMapping("/by-training/{trainingCode}/registrations")
    public ResponseEntity<?> getRegistrationsByTraining(
            @PathVariable String trainingCode) {

        log.info("HTTP GET /api/trainings/by-training/{}/registrations", trainingCode);
        return trainingService.getRegistrationsByTraining(trainingCode);
    }


 // Student: get all registrations by roll number
    @GetMapping("/registrations/{rollNumber}")
    public ResponseEntity<?> getRegistrationsByRollNumber(
            @PathVariable String rollNumber) {

        log.info("HTTP GET /api/trainings/registrations/{}", rollNumber);
        return trainingService.getRegistrationsByRollNumber(rollNumber);
    }

    // Student: fetch single registration by training + roll number
    @GetMapping("/{trainingCode}/registrations/{rollNumber}")
    public ResponseEntity<?> getRegistrationByTrainingAndStudent(
            @PathVariable String trainingCode,
            @PathVariable String rollNumber) {

        log.info(
            "HTTP GET /api/trainings/{}/registrations/{} | student registration view",
            trainingCode,
            rollNumber
        );

        return trainingService.getRegistrationByTrainingAndStudent(
                trainingCode, rollNumber);
    }
    
    @PostMapping("/{trainingCode}/pay")
    public ResponseEntity<?> payForTraining(
            @PathVariable String trainingCode) {

        log.info("HTTP POST /api/trainings/{}/pay", trainingCode);
        return trainingService.initiatePayment(trainingCode);
    }

}
