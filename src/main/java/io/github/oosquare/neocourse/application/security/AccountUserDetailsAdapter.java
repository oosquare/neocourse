package io.github.oosquare.neocourse.application.security;

import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.oosquare.neocourse.domain.account.model.Account;

@Getter
@AllArgsConstructor(staticName = "of")
public class AccountUserDetailsAdapter implements UserDetails {

    private final @NonNull Account account;

    @Override
    public String getUsername() {
        return this.account.getUsername().getValue();
    }

    @Override
    public String getPassword() {
        return this.account.getEncodedPassword().getValue();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authority = switch (this.account.getKind()) {
            case STUDENT -> Roles.toGrantedAuthority(Roles.STUDENT);
            case TEACHER -> Roles.toGrantedAuthority(Roles.TEACHER);
            case ADMINISTRATOR -> Roles.toGrantedAuthority(Roles.ADMINISTRATOR);
        };
        return List.of(authority);
    }
}
