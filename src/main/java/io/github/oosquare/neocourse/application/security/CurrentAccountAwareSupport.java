package io.github.oosquare.neocourse.application.security;

import org.springframework.security.core.context.SecurityContextHolder;

import io.github.oosquare.neocourse.domain.account.model.Account;

public interface CurrentAccountAwareSupport {

    default Account getCurrentAccount() {
        var principal = (AccountUserDetailsAdapter) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        return principal.getAccount();
    }
}
