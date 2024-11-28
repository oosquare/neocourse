package io.github.oosquare.neocourse.application.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class Roles {

    private Roles() {}

    public static final String STUDENT = "STUDENT";
    public static final String TEACHER = "TEACHER";
    public static final String ADMINISTRATOR = "ADMINISTRATOR";

    public static GrantedAuthority toGrantedAuthority(String role) {
        return new SimpleGrantedAuthority("ROLE_" + role);
    }
}
