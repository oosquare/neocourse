package io.github.oosquare.neocourse.application.security;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.exception.UserAuthorizationException;

@Service
@AllArgsConstructor
public class AccountSecurityService implements UserDetailsService {

    private final @NonNull PasswordEncoder passwordEncoder;
    private final @NonNull AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.accountRepository.findByUsername(Username.of(username))
            .map(AccountUserDetailsAdapter::of)
            .orElseThrow(() -> new UsernameNotFoundException("Could not find username " + username));
    }

    public void checkPasswordCorrect(@NonNull Account account, @NonNull String password) {
        if (!this.passwordEncoder.matches(password, account.getEncodedPassword().getValue())) {
            throw UserAuthorizationException.builder()
                .message("Password for this account is not correct")
                .userMessage("Password is not correct")
                .context("account.id", account.getId())
                .context("account.username", account.getUsername())
                .build();
        }
    }
}
