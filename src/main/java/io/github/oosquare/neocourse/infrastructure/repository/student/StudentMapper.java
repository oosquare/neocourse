package io.github.oosquare.neocourse.infrastructure.repository.student;

import jakarta.persistence.EntityManager;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.infrastructure.repository.DataMapper;

@Repository
public class StudentMapper extends DataMapper<StudentData> {

    public StudentMapper(@NonNull EntityManager entityManager) {
        super(entityManager, StudentData.class);
    }

    public Optional<StudentData> findByUsername(@NonNull String username) {
        var data = this.getEntityManager()
            .createNamedQuery("StudentData.findByUsername", this.getDataClass())
            .setParameter("username", username)
            .setMaxResults(1)
            .getResultList();
        return (data.isEmpty() ? Optional.empty() : Optional.of(data.getFirst()));

    }
}
