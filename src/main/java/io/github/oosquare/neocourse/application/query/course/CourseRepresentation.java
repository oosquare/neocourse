package io.github.oosquare.neocourse.application.query.course;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.infrastructure.repository.course.CourseData;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class CourseRepresentation {

    private String id;
    private String courseName;
    private Integer classPeriod;

    public static CourseRepresentation fromData(@NonNull CourseData data) {
        return CourseRepresentation.builder()
            .id(data.getId())
            .courseName(data.getName())
            .classPeriod(data.getClassPeriod())
            .build();
    }
}
