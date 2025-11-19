package com.techcode.studentmgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class studentmgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(studentmgmtApplication.class, args);
	}

}
