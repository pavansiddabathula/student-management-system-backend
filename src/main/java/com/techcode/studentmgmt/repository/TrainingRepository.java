package com.techcode.studentmgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techcode.studentmgmt.entity.Training;
import com.techcode.studentmgmt.entity.TrainingRegistration;
import com.techcode.studentmgmt.enums.TrainingMode;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    Optional<Training> findByTrainingCode(String trainingCode);

    List<Training> findByMode(TrainingMode mode);
    
  

}
