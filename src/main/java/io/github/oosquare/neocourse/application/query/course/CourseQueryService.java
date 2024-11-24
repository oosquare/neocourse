package io.github.oosquare.neocourse.application.query.course;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.infrastructure.repository.course.CourseMapper;
import io.github.oosquare.neocourse.utility.exception.EntityNotFoundException;
import io.github.oosquare.neocourse.utility.id.Id;

@Service
@AllArgsConstructor
@Slf4j
public class CourseQueryService {

    private final @NonNull CourseMapper courseMapper;

    @Transactional
    public CourseRepresentation getCourseById(@NonNull Id courseId, @NonNull Account account) {
        log.info("{} requests getCourseById with {}", account.toLoggingString(), courseId);

        return this.courseMapper.find(courseId.getValue())
            .map(CourseRepresentation::fromData)
            .orElseThrow(() -> EntityNotFoundException.builder()
                .entity(CourseRepresentation.class)
                .context("courseId", courseId)
                .build());
    }

    @Transactional
    public List<CourseRepresentation> getAllCourses(@NonNull Account account) {
        log.info("{} requests getAllCourses", account.toLoggingString());

        return this.courseMapper.findAll()
            .stream()
            .map(CourseRepresentation::fromData)
            .toList();
    }
}
