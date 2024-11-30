package io.github.oosquare.neocourse.infrastructure.repository.account;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Embeddable
public class AccountRoleData {

    @Column(nullable = false)
    private AccountRoleKindData roleKind;

    @Column(nullable = false)
    private String userDataId;
}
