package io.github.oosquare.neocourse.infrastructure.repository.course;

import jakarta.persistence.EntityManager;
import java.util.List;
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
        var data = this.getEntityManager()
            .createNamedQuery("CourseData.findByName", this.getDataClass())
            .setParameter("name", name)
            .setMaxResults(1)
            .getResultList();
        return (data.isEmpty() ? Optional.empty() : Optional.of(data.getFirst()));
    }

    public List<CourseData> findAll() {
        return this.getEntityManager().
            createNamedQuery("CourseData.findAll", this.getDataClass())
            .getResultList();
    }
}
