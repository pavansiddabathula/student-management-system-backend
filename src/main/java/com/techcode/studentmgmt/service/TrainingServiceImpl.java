package com.techcode.studentmgmt.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.techcode.studentmgmt.constants.ErrorCodeEnums;
import com.techcode.studentmgmt.constants.SuccessMessageConstants;
import com.techcode.studentmgmt.dto.requestdto.CreateTrainingRequest;
import com.techcode.studentmgmt.dto.requestdto.PayPalOrderRequest;
import com.techcode.studentmgmt.dto.responsedto.PayPalOrderResponse;
import com.techcode.studentmgmt.dto.responsedto.SuccessResponse;
import com.techcode.studentmgmt.entity.StudentInfo;
import com.techcode.studentmgmt.entity.Training;
import com.techcode.studentmgmt.entity.TrainingRegistration;
import com.techcode.studentmgmt.enums.RegistrationStatus;
import com.techcode.studentmgmt.enums.TrainingMode;
import com.techcode.studentmgmt.exceptions.BusinessException;
import com.techcode.studentmgmt.exceptions.ValidationException;
import com.techcode.studentmgmt.modelmappers.TrainingRegistrationMapper;
import com.techcode.studentmgmt.modelmappers.TrainingsMapper;
import com.techcode.studentmgmt.repository.StudentRepository;
import com.techcode.studentmgmt.repository.TrainingRegistrationRepository;
import com.techcode.studentmgmt.repository.TrainingRepository;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final StudentRepository studentRepository;
    private final TrainingRegistrationRepository registrationRepository;
    
    private final Validator validator;
    
    private final RestTemplate restTemplate;
    

    // ================= CREATE TRAINING =================
    @Override
    public ResponseEntity<?> createTraining(CreateTrainingRequest request) {

        log.info("createTraining | trainingName={}", request.getTrainingName());

        validateTraining(request);

        Training training = TrainingsMapper.toEntity(request, "ADMIN");
        Training saved = trainingRepository.save(training);

        log.info("Training created successfully | trainingCode={}", saved.getTrainingCode());

        return success(
                SuccessMessageConstants.TRAINING_CREATE_SUCCESS.format(saved.getTrainingCode()),
                TrainingsMapper.toResponse(saved),
                HttpStatus.CREATED
        );
    }

    // ================= UPDATE TRAINING =================
    @Override
    public ResponseEntity<?> updateTraining(String trainingCode, CreateTrainingRequest request) {

        log.info("updateTraining | trainingCode={}", trainingCode);

        Training training = trainingRepository.findByTrainingCode(trainingCode)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodeEnums.TRAINING_NOT_FOUND, trainingCode)
                );

        validateTraining(request);

        training.setTrainingName(request.getTrainingName());
        training.setDescription(request.getDescription());
        training.setDurationInWeeks(request.getDurationInWeeks());
        training.setMode(request.getMode());
        training.setLevel(request.getLevel());
        training.setStartDate(request.getStartDate());
        training.setEndDate(request.getEndDate());
        training.setSessionTiming(request.getSessionTiming());
        training.setTotalSeats(request.getTotalSeats());
        training.setPrice(request.getPrice());
        training.setTrainerName(request.getTrainerName());
        training.setSyllabusPdfUrl(request.getSyllabusPdfUrl());
        training.setUpdatedAt(LocalDateTime.now());

        Training updated = trainingRepository.save(training);

        log.info("Training updated successfully | trainingCode={}", trainingCode);

        return success(
                SuccessMessageConstants.TRAINING_UPDATE_SUCCESS.format(trainingCode),
                TrainingsMapper.toResponse(updated),
                HttpStatus.OK
        );
    }

    // ================= GET ALL TRAININGS =================
    @Override
    public ResponseEntity<?> getAllTrainings() {

        log.info("getAllTrainings called");

        List<?> trainings = trainingRepository.findAll()
                .stream()
                .map(TrainingsMapper::toResponse)
                .toList();

        log.info("Total trainings fetched={}", trainings.size());

        return success(
                SuccessMessageConstants.TRAINING_FETCH_SUCCESS.format(trainings.size()),
                trainings,
                HttpStatus.OK
        );
    }

    // ================= GET TRAINING BY CODE =================
    @Override
    public ResponseEntity<?> getTrainingByCode(String trainingCode) {

        log.info("getTrainingByCode | trainingCode={}", trainingCode);

        Training training = trainingRepository.findByTrainingCode(trainingCode)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodeEnums.TRAINING_NOT_FOUND, trainingCode)
                );

        return success(
                SuccessMessageConstants.TRAINING_FETCH_SUCCESS.format(trainingCode),
                TrainingsMapper.toResponse(training),
                HttpStatus.OK
        );
    }

    // ================= GET TRAININGS BY MODE =================
    @Override
    public ResponseEntity<?> getTrainingsByMode(TrainingMode mode) {

        log.info("getTrainingsByMode | mode={}", mode);

        List<?> trainings = trainingRepository.findByMode(mode)
                .stream()
                .map(TrainingsMapper::toResponse)
                .toList();

        log.info("Trainings fetched for mode {} = {}", mode, trainings.size());

        return success(
                SuccessMessageConstants.TRAINING_FETCH_SUCCESS.format(trainings.size()),
                trainings,
                HttpStatus.OK
        );
    }

    // ================= REGISTER TRAINING =================
    @Override
    public ResponseEntity<?> registerTraining(String trainingCode) {

        log.info("registerTraining | trainingCode={}", trainingCode);

        Training training = trainingRepository.findByTrainingCode(trainingCode)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodeEnums.TRAINING_NOT_FOUND, trainingCode)
                );

        // TODO: Replace with JWT later
        String rollNumber = "21481A05K2";

        StudentInfo student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber)
                );

        if (registrationRepository.existsByTrainingCodeAndRollNumber(trainingCode, rollNumber)) {
            throw new BusinessException(
                    ErrorCodeEnums.DUPLICATE_TRAINING_REGISTRATION,
                    rollNumber,
                    trainingCode
            );
        }

        boolean paymentRequired = training.getPrice().compareTo(BigDecimal.ZERO) > 0;

        RegistrationStatus status = paymentRequired
                ? RegistrationStatus.PAYMENT_PENDING
                : RegistrationStatus.REGISTERED;

        TrainingRegistration registration = TrainingRegistration.builder()
                .trainingCode(training.getTrainingCode())
                .trainingName(training.getTrainingName())
                .trainingPrice(training.getPrice())
                .rollNumber(student.getRollNumber())
                .studentName(student.getFirstName() + " " + student.getLastName())
                .registrationStatus(status)
                .paid(false)
                .amountPaid(BigDecimal.ZERO)
                .registeredAt(LocalDateTime.now())
                .build();

        log.info("Saving training registration | rollNumber={}, trainingCode={}",
                rollNumber, trainingCode);

        TrainingRegistration saved = registrationRepository.save(registration);

        log.info("Registration completed | rollNumber={}, trainingCode={}",
                rollNumber, trainingCode);

        return success(
                SuccessMessageConstants.TRAINING_REGISTER_SUCCESS.format(rollNumber, trainingCode),
                TrainingRegistrationMapper.toResponse(saved, paymentRequired),
                HttpStatus.CREATED
        );
    }

    // ================= ADMIN: VIEW REGISTRATIONS =================
    @Override
    public ResponseEntity<?> getRegistrationsByTraining(String trainingCode) {

        log.info("getRegistrationsByTraining | trainingCode={}", trainingCode);

        trainingRepository.findByTrainingCode(trainingCode)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodeEnums.TRAINING_NOT_FOUND, trainingCode)
                );

        List<?> registrations = registrationRepository.findByTrainingCode(trainingCode)
                .stream()
                .map(r -> TrainingRegistrationMapper.toResponse(
                        r, r.getRegistrationStatus() == RegistrationStatus.PAYMENT_PENDING))
                .toList();

        log.info("Total registrations fetched={}", registrations.size());

        return success(
                SuccessMessageConstants.TRAINING_REGISTRATION_FETCH_SUCCESS.format(trainingCode),
                registrations,
                HttpStatus.OK
        );
    }

    // ================= STUDENT: VIEW BY ROLL =================
    @Override
    public ResponseEntity<?> getRegistrationsByRollNumber(String rollNumber) {

        log.info("getRegistrationsByRollNumber | rollNumber={}", rollNumber);

        studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodeEnums.STUDENT_NOT_FOUND, rollNumber)
                );

        List<?> responses = registrationRepository.findByRollNumber(rollNumber)
                .stream()
                .map(r -> TrainingRegistrationMapper.toResponse(
                        r, r.getRegistrationStatus() == RegistrationStatus.PAYMENT_PENDING))
                .toList();

        log.info("Registrations found for rollNumber={} count={}",
                rollNumber, responses.size());

        return success(
                SuccessMessageConstants.TRAINING_REGISTRATION_FETCH_SUCCESS.format(rollNumber),
                responses,
                HttpStatus.OK
        );
    }

    // ================= STUDENT: VIEW SINGLE REGISTRATION =================
    @Override
    public ResponseEntity<?> getRegistrationByTrainingAndStudent(
            String trainingCode, String rollNumber) {

        log.info("getRegistrationByTrainingAndStudent | trainingCode={}, rollNumber={}",
                trainingCode, rollNumber);

        TrainingRegistration registration = registrationRepository
                .findByTrainingCodeAndRollNumber(trainingCode, rollNumber)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCodeEnums.TRAINING_REGISTRATION_NOT_FOUND,
                                rollNumber,
                                trainingCode
                        )
                );

        boolean paymentRequired =
                registration.getRegistrationStatus() == RegistrationStatus.PAYMENT_PENDING;

        return success(
                SuccessMessageConstants.TRAINING_REGISTRATION_FETCH_SUCCESS
                        .format(rollNumber, trainingCode),
                TrainingRegistrationMapper.toResponse(registration, paymentRequired),
                HttpStatus.OK
        );
    }

    // ================= VALIDATION =================
    private void validateTraining(CreateTrainingRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        validator.validate(request)
                .forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));

        if (request.getStartDate() != null && request.getEndDate() != null &&
                request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessException(ErrorCodeEnums.INVALID_TRAINING_DATES);
        }

        if (request.getPrice() != null && request.getPrice().signum() < 0) {
            throw new BusinessException(ErrorCodeEnums.INVALID_TRAINING_PRICE);
        }

        if (!errors.isEmpty()) {
            log.error("Training validation failed | errors={}", errors);
            throw new ValidationException(errors);
        }
    }
    
    

    // ================= SUCCESS RESPONSE =================
    private ResponseEntity<?> success(String message, Object data, HttpStatus status) {

        return ResponseEntity.status(status).body(
                SuccessResponse.builder()
                        .status("SUCCESS")
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
    @Override
    public ResponseEntity<?> initiatePayment(String trainingCode) {

        log.info("initiatePayment | trainingCode={}", trainingCode);

        Training training = trainingRepository.findByTrainingCode(trainingCode)
                .orElseThrow(() -> new BusinessException(
                        ErrorCodeEnums.TRAINING_NOT_FOUND,
                        trainingCode
                ));

        // Free training → no payment needed
        if (training.getPrice().compareTo(BigDecimal.ZERO) == 0) {
            log.info("Free training | payment not required | trainingCode={}", trainingCode);

            return success(
                    "This is a free training. No payment required.",
                    null,
                    HttpStatus.OK
            );
        }

        // Generate transaction reference
        String tnref = "TRN-" + trainingCode + "-" + System.currentTimeMillis();

        // Hardcoded URLs for now (acceptable at this stage)
        String returnUrl = "http://localhost:8080/api/payments/paypal/success";
        String cancelUrl = "http://localhost:8080/api/payments/paypal/cancel";

        // Build request object using DB values
        PayPalOrderRequest paypalRequest = new PayPalOrderRequest(
                tnref,
                training.getPrice().toPlainString(),   // DB → String
                "USD",                                 // or training.getCurrency()
                returnUrl,
                cancelUrl
        );

        log.info(
                "Creating PayPal order | tnref={} | amount={} {}",
                tnref,
                paypalRequest.getAmount(),
                paypalRequest.getCurrency()
        );

        // Call PayPal provider service
        PayPalOrderResponse paypalResponse =createOrder(paypalRequest);

        log.info(
                "PayPal order created | orderId={} | status={}",
                paypalResponse.getOrderId(),
                paypalResponse.getPaypalStatus()
        );

        return success(
                "Payment initiated successfully",
                Map.of(
                        "trainingCode", trainingCode,
                        "amount", paypalRequest.getAmount(),
                        "currency", paypalRequest.getCurrency(),
                        "approvalUrl", paypalResponse.getRedirectUrl()
                ),
                HttpStatus.OK
        );
    }
    
    public PayPalOrderResponse createOrder(PayPalOrderRequest request) {

        return restTemplate.postForObject(
                "http://localhost:8081/v1/paypal/order",
                request,
                PayPalOrderResponse.class
        );
    }





}
