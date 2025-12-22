package com.techcode.studentmgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techcode.studentmgmt.entity.TrainingRegistration;

public interface TrainingRegistrationRepository extends JpaRepository<TrainingRegistration, Long> {

	boolean existsByTrainingCodeAndRollNumber(String trainingCode, String rollNumber);

	List<TrainingRegistration> findByTrainingCode(String trainingCode);

	List<TrainingRegistration> findByRollNumber(String rollNumber);

	Optional<TrainingRegistration> findByTrainingCodeAndRollNumber(String trainingCode, String rollNumber);
}
