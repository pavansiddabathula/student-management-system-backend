package com.techcode.studentmgmt.modelmappers;

import com.techcode.studentmgmt.dto.requestdto.StudentRequest;
import com.techcode.studentmgmt.dto.responsedto.StudentResponse;
import com.techcode.studentmgmt.entity.StudentInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StudentMapper {

    // Convert Request DTO → Entity
    public static StudentInfo toEntity(StudentRequest request) {
    
         return  StudentInfo.builder()
                .rollNumber(request.getRollNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .branch(request.getBranch())
                .phoneNumber(request.getPhoneNumber())
                .build();

    }

    // Convert Entity → Response DTO
    public static StudentResponse toResponse(StudentInfo entity) {
        return   StudentResponse.builder()
       		    .id(entity.getId())
				.rollNumber(entity.getRollNumber())
				.fullName(entity.getFirstName() + " " + entity.getLastName())
				.email(entity.getEmail())
				.branch(entity.getBranch())
				.phoneNumber(entity.getPhoneNumber())
				.build();
     
    }
}
