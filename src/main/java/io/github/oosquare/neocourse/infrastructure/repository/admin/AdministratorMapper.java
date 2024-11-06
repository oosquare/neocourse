package io.github.oosquare.neocourse.infrastructure.repository.admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.infrastructure.repository.DataMapper;

@Repository
public class AdministratorMapper extends DataMapper<AdministratorData> {

    public AdministratorMapper(@NonNull EntityManager entityManager) {
        super(entityManager, AdministratorData.class);
    }

    public Optional<AdministratorData> findByUsername(@NonNull String username) {
        try {
            var data = this.getEntityManager()
                .createNamedQuery("findByUsername", this.getDataClass())
                .setParameter("username", username)
                .getSingleResult();
            return Optional.of(data);
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }
}
