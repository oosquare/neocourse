package io.github.oosquare.neocourse.application.security;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.common.model.Username;

@Service
@AllArgsConstructor
@Profile("production")
public class AccountService implements UserDetailsService {

    private final @NonNull AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.accountRepository.findByUsername(Username.of(username))
            .map(AccountUserDetailsAdapter::of)
            .orElseThrow(() -> new UsernameNotFoundException("Could not find username " + username));
    }
}
