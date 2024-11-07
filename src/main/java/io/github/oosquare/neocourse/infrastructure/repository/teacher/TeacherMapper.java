package io.github.oosquare.neocourse.infrastructure.repository.teacher;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.infrastructure.repository.DataMapper;

@Repository
public class TeacherMapper extends DataMapper<TeacherData> {

    public TeacherMapper(@NonNull EntityManager entityManager) {
        super(entityManager, TeacherData.class);
    }

    public Optional<TeacherData> findByUsername(@NonNull String username) {
        try {
            var data = this.getEntityManager()
                .createNamedQuery("TeacherData.findByUsername", this.getDataClass())
                .setParameter("username", username)
                .getSingleResult();
            return Optional.of(data);
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }
}
