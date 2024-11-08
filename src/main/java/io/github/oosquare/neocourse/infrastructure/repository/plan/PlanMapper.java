package io.github.oosquare.neocourse.infrastructure.repository.plan;

import jakarta.persistence.EntityManager;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.infrastructure.repository.DataMapper;

@Repository
public class PlanMapper extends DataMapper<PlanData> {

    public PlanMapper(@NonNull EntityManager entityManager) {
        super(entityManager, PlanData.class);
    }

    public Optional<PlanData> findByName(@NonNull String name) {
        var data = this.getEntityManager()
            .createNamedQuery("PlanData.findByName", this.getDataClass())
            .setParameter("name", name)
            .setMaxResults(1)
            .getResultList();
        return (data.isEmpty() ? Optional.empty() : Optional.of(data.getFirst()));
    }

    public Optional<PlanData> findByIncludedCourse(@NonNull String includedCourse) {
        var data = this.getEntityManager()
            .createNamedQuery("PlanData.findByIncludedCourse", this.getDataClass())
            .setParameter("includedCourse", includedCourse)
            .setMaxResults(1)
            .getResultList();
        return (data.isEmpty() ? Optional.empty() : Optional.of(data.getFirst()));
    }
}
