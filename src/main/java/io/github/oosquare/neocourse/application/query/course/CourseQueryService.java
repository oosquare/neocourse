package io.github.oosquare.neocourse.application.query.course;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.infrastructure.repository.course.CourseMapper;

@Service
@AllArgsConstructor
@Slf4j
public class CourseQueryService {

    private final @NonNull CourseMapper courseMapper;

    @Transactional
    public List<CourseRepresentation> getAllCourses() {
        log.info("Requests getAllCourses");

        return this.courseMapper.findAll()
            .stream()
            .map(CourseRepresentation::fromData)
            .toList();
    }
}
