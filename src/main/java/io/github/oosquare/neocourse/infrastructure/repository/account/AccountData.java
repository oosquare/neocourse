package io.github.oosquare.neocourse.infrastructure.repository.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

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
@Entity
@Table(name = "account")
@NamedQuery(
    name = "AccountData.findByUsername",
    query = "SELECT a FROM AccountData a WHERE a.username = :username"
)
public class AccountData {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @Column(nullable = false, updatable = false)
    private AccountKindData kind;

    @Column(nullable = false, updatable = false, unique = true)
    private String username;

    @Column(nullable = false, updatable = false)
    private String displayedUsername;

    @Column(nullable = false, updatable = false)
    private String encodedPassword;

    @Column(nullable = false, updatable = false)
    private String userId;
}
