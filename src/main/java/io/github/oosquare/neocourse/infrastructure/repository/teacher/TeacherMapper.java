package io.github.oosquare.neocourse.infrastructure.repository.teacher;

import jakarta.persistence.EntityManager;
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
        var data = this.getEntityManager()
            .createNamedQuery("TeacherData.findByUsername", this.getDataClass())
            .setParameter("username", username)
            .setMaxResults(1)
            .getResultList();
        return (data.isEmpty() ? Optional.empty() : Optional.of(data.getFirst()));
    }
}
