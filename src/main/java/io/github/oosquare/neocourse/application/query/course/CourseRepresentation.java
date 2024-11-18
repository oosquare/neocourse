package io.github.oosquare.neocourse.application.query.course;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.infrastructure.repository.course.CourseData;

@Value
@Builder
public class CourseRepresentation {

    private String id;
    private String name;
    private Integer classPeriod;

    public static CourseRepresentation fromData(@NonNull CourseData data) {
        return CourseRepresentation.builder()
            .id(data.getId())
            .name(data.getName())
            .classPeriod(data.getClassPeriod())
            .build();
    }
}