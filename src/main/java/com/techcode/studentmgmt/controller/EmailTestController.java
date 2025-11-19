package com.techcode.studentmgmt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techcode.studentmgmt.utils.EmailUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailUtil emailUtil;

    @GetMapping("/test-mail")
    public String sendMail() {
        emailUtil.sendPasswordMail(
                "jayaramsiddabathula37@gmail.com",
                "Pavan",
                "TEST123",
                "Temp@1234",
                "ADMIN"
        );
        return "Mail Sent";
    }
}
