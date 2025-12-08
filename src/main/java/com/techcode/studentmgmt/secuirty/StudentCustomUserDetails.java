package com.techcode.studentmgmt.secuirty;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.techcode.studentmgmt.entity.StudentInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StudentCustomUserDetails implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final StudentInfo student;

    public StudentCustomUserDetails(StudentInfo student) {
        this.student = student;
        log.info("🎓 StudentCustomUserDetails created for rollNumber: {}", student.getRollNumber());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("🎭 Assigning authority: ROLE_STUDENT");
        return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }

    @Override
    public String getPassword() {
        return student.getPassword();  // encrypted in DB
    }

    @Override
    public String getUsername() {
        return student.getRollNumber();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public String getName() {
        return student.getFirstName() + " " + student.getLastName();
    }

	private String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}
}
