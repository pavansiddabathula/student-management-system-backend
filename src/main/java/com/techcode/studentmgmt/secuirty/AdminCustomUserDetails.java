package com.techcode.studentmgmt.secuirty;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.techcode.studentmgmt.entity.AdminInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminCustomUserDetails implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final AdminInfo admin;

    public AdminCustomUserDetails(AdminInfo admin) {
        this.admin = admin;
        log.info("🔐 AdminCustomUserDetails created for adminId: {}", admin.getAdminId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("🎭 Assigning authority: ROLE_ADMIN");
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getPassword() {
        return admin.getPassword(); // encrypted in DB
    }

    @Override
    public String getUsername() {
        return admin.getAdminId();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public String getName() {
        return admin.getName();
    }
}
