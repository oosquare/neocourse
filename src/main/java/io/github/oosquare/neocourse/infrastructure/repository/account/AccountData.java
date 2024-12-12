package io.github.oosquare.neocourse.infrastructure.repository.account;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.util.Set;

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
@NamedQuery(
    name = "AccountData.findAllReturningSummaryProjection",
    query = """
        SELECT new io.github.oosquare.neocourse.infrastructure.repository.account.AccountSummaryProjection(
            a.id,
            a.username,
            a.displayedUsername
        ) FROM AccountData a
    """
)
@NamedQuery(
    name = "AccountData.findById",
    query = "SELECT a FROM AccountData a WHERE a.id = :accountId"
)
public class AccountData {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @Column(nullable = false, updatable = false, unique = true)
    private String username;

    @Column(nullable = false, updatable = false)
    private String displayedUsername;

    @Column(nullable = false, updatable = false)
    private String encodedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "account_roles_id", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "role_id", nullable = false)
    private Set<AccountRoleData> roles;
}
