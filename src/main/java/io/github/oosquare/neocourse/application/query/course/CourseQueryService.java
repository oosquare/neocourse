package io.github.oosquare.neocourse.application.query.course;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.infrastructure.repository.course.CourseMapper;

@Service
@AllArgsConstructor
public class CourseQueryService {

    private final @NonNull CourseMapper courseMapper;

    @Transactional
    public List<CourseRepresentation> getAllCourses() {
        return this.courseMapper.findAll()
            .stream()
            .map(CourseRepresentation::fromData)
            .toList();
    }
}
