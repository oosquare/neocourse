package io.github.oosquare.neocourse.infrastructure.repository.course;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
public class CourseConverter implements DataConverter<Course, CourseData> {

    @Override
    public Course convertToDomain(@NonNull CourseData data) {
        return Course.builder()
            .id(Id.of(data.getId()))
            .name(CourseName.of(data.getName()))
            .classPeriod(ClassPeriod.of(data.getClassPeriod()))
            .build();
    }

    @Override
    public CourseData convertToData(@NonNull Course entity) {
        return CourseData.builder()
            .id(entity.getId().getValue())
            .name(entity.getName().getValue())
            .classPeriod(entity.getClassPeriod().getValue())
            .build();
    }
}
