package io.github.oosquare.neocourse.infrastructure.repository.course;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.utility.id.Id;

@Repository
@AllArgsConstructor
public class CourseConcreteRepository implements CourseRepository {

    private final @NonNull CourseMapper courseMapper;
    private final @NonNull CourseConverter courseConverter;

    @Override
    public Optional<Course> findByName(@NonNull CourseName name) {
        return this.courseMapper.findByName(name.getValue())
            .map(this.courseConverter::convertToDomain);
    }

    @Override
    public Optional<Course> find(@NonNull Id id) {
        return this.courseMapper.find(id.getValue())
            .map(this.courseConverter::convertToDomain);
    }

    @Override
    public void save(@NonNull Course entity) {
        var data = this.courseConverter.convertToData(entity);
        this.courseMapper.save(data);
    }

    @Override
    public void remove(@NonNull Course entity) {
        var data = this.courseConverter.convertToData(entity);
        this.courseMapper.remove(data);
    }
}
