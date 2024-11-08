package io.github.oosquare.neocourse.infrastructure.repository.admin;

import jakarta.persistence.EntityManager;
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
        var data = this.getEntityManager()
            .createNamedQuery("AdministratorData.findByUsername", this.getDataClass())
            .setParameter("username", username)
            .setMaxResults(1)
            .getResultList();
        return (data.isEmpty() ? Optional.empty() : Optional.of(data.getFirst()));
    }
}
