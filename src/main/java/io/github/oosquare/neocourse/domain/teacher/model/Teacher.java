package io.github.oosquare.neocourse.domain.teacher.model;

import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.User;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.teacher.exception.OfferedCourseException;
import io.github.oosquare.neocourse.domain.teacher.exception.TeacherException;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
public class Teacher implements User {

    private final @NonNull Id id;
    private final @NonNull Username username;
    private final @NonNull DisplayedUsername displayedUsername;
    private @NonNull OfferedCourses offeredCourses;

    public Teacher(
        @NonNull Id id,
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername
    ) {
        this.id = id;
        this.username = username;
        this.displayedUsername = displayedUsername;
        this.offeredCourses = new OfferedCourses();
    }

    public void addOfferedCourse(@NonNull Course course) {
        try {
            this.offeredCourses = this.offeredCourses.addCourse(course.getId());
        } catch (OfferedCourseException exception) {
            throw new TeacherException(
                String.format(
                    "Could not add Course[id=%s] for Teacher[id=%s]",
                    course.getId(),
                    this.getId()
                ),
                exception
            );
        }
    }

    public void removeOfferedCourse(@NonNull Course course) {
        try {
            this.offeredCourses = this.offeredCourses.removeCourse(course.getId());
        } catch (OfferedCourseException exception) {
            throw new TeacherException(
                String.format(
                    "Could not remove Course[id=%s] for Teacher[id=%s]",
                    course.getId(),
                    this.getId()
                ),
                exception
            );
        }
    }
}
