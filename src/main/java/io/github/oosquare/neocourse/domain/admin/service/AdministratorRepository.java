package io.github.oosquare.neocourse.domain.admin.service;

import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.domain.EntityRepository;
import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.common.model.Username;

public interface AdministratorRepository extends EntityRepository<Administrator> {

    Optional<Administrator> findByUsername(@NonNull Username username);

    @Override
    default Class<Administrator> getEntityClass() {
        return Administrator.class;
    }
}
