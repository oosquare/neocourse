package io.github.oosquare.neocourse.infrastructure.repository.course;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.infrastructure.repository.DataMapper;

@Repository
public class CourseMapper extends DataMapper<CourseData> {

    public CourseMapper(@NonNull EntityManager entityManager) {
        super(entityManager, CourseData.class);
    }

    public Optional<CourseData> findByName(@NonNull String name) {
        try {
            var data = this.getEntityManager()
                .createNamedQuery("CourseData.findByName", this.getDataClass())
                .setParameter("name", name)
                .getSingleResult();
            return Optional.of(data);
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }
}
