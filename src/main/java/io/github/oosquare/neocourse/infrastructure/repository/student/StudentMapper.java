package io.github.oosquare.neocourse.infrastructure.repository.student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
        try {
            var data = this.getEntityManager()
                .createNamedQuery("StudentData.findByUsername", this.getDataClass())
                .setParameter("username", username)
                .getSingleResult();
            return Optional.of(data);
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }
}
