package com.techcode.studentmgmt.constants;



public enum JobStatus {
    
        DRAFT,      // Created but not visible to students
        OPEN,       // Students can see and apply
        CLOSED,     // Manually closed by admin
        EXPIRED     // Automatically expired based on expiresAt
   

}
