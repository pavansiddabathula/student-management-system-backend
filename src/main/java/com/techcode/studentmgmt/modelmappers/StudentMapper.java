package com.techcode.studentmgmt.modelmappers;

import com.techcode.studentmgmt.dto.requestdto.StudentRequest;
import com.techcode.studentmgmt.dto.rsponsedto.StudentResponse;
import com.techcode.studentmgmt.entity.StudentInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StudentMapper {

    // Convert Request DTO → Entity
    public static StudentInfo toEntity(StudentRequest request) {
        StudentInfo studentInfo = StudentInfo.builder()
                .rollNumber(request.getRollNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .branch(request.getBranch())
                .username(request.getUsername())
                .password(request.getPassword())// encode password
                .phoneNumber(request.getPhoneNumber())
                .build();

        log.info("StudentMapper::Mapped StudentInfo Entity: {}", studentInfo);
        return studentInfo;
    }

    // Convert Entity → Response DTO
    public static StudentResponse toResponse(StudentInfo entity) {
        StudentResponse response = StudentResponse.builder()
       		 .id(entity.getId())
				.rollNumber(entity.getRollNumber())
				.firstName(entity.getFirstName())
				.lastName(entity.getLastName())
				.email(entity.getEmail())
				.branch(entity.getBranch())
				.username(entity.getUsername())
				.phoneNumber(entity.getPhoneNumber())
				.build();
           	 
        log.info("StudentMapper::Mapped StudentResponse: {}", response);
        return response;
    }
}
