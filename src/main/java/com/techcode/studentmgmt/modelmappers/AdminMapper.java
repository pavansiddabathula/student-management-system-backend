package com.techcode.studentmgmt.modelmappers;

import com.techcode.studentmgmt.dto.requestdto.AdminRequest;
import com.techcode.studentmgmt.dto.responsedto.AdminResponse;
import com.techcode.studentmgmt.entity.AdminInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminMapper {

    // Convert AdminRequest → AdminInfo Entity
    public static AdminInfo toEntity(AdminRequest request) {
   
       return AdminInfo.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
       

    }

    // Convert AdminInfo Entity → AdminResponse DTO
    public static AdminResponse toResponse(AdminInfo admin) {
        return AdminResponse.builder()
                .adminId(admin.getAdminId())
                .name(admin.getName())
                .email(admin.getEmail())
                .phoneNumber(admin.getPhoneNumber())
                .role(admin.getRole())
                .build();

    }
}
