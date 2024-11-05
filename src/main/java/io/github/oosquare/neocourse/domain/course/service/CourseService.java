package io.github.oosquare.neocourse.domain.course.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.course.exception.RemoveCourseException;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;

@Service
@AllArgsConstructor
public class CourseService {

    private final @NonNull PlanRepository planRepository;
    private final @NonNull ScheduleRepository scheduleRepository;
    private final @NonNull CourseFactory courseFactory;

    public Course addCourse(@NonNull CourseName courseName, @NonNull ClassPeriod classPeriod) {
        return this.courseFactory.createCourse(courseName, classPeriod);
    }

    public void prepareRemovingCourse(@NonNull Course course) {
        this.checkNotIncludedInPlan(course);
        this.checkNotAssociatedWithSchedule(course);
    }

    private void checkNotIncludedInPlan(Course course) {
        this.planRepository.findByIncludedCourse(course)
            .ifPresent(plan -> {
                throw new RemoveCourseException(String.format(
                    "Course[id=%s, name=%s] is included in Plan[id=%s, name=%s]",
                    course.getId(),
                    course.getName(),
                    plan.getId(),
                    plan.getName()
                ));
            });
    }

    private void checkNotAssociatedWithSchedule(Course course) {
        this.scheduleRepository.findByCourse(course)
            .ifPresent(schedule -> {
                throw new RemoveCourseException(String.format(
                    "Course[id=%s, name=%s] is associated with Schedule[id=%s, time=%s, place=%s]",
                    course.getId(),
                    course.getName(),
                    schedule.getId(),
                    schedule.getTime().getStart(),
                    schedule.getPlace()
                ));
            });
    }
}
