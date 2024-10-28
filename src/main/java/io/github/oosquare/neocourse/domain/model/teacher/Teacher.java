package io.github.oosquare.neocourse.domain.model.teacher;

import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.model.common.DisplayedUsername;
import io.github.oosquare.neocourse.domain.model.common.User;
import io.github.oosquare.neocourse.domain.model.common.Username;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
public class Teacher extends User {

    private @NonNull OfferedCourses offeredCourses;

    public Teacher(
        @NonNull Id id,
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername
    ) {
        super(id, username, displayedUsername);
        this.offeredCourses = new OfferedCourses();
    }

    public void addOfferedCourse(@NonNull Id course) {
        try {
            this.offeredCourses = this.offeredCourses.addCourse(course);
        } catch (OfferedCourseException exception) {
            throw new TeacherException(
                String.format("Could not add Course[id=%s] for Teacher[id=%s]", course, this.getId()),
                exception
            );
        }
    }

    public void removeOfferedCourse(@NonNull Id course) {
        try {
            this.offeredCourses = this.offeredCourses.removeCourse(course);
        } catch (OfferedCourseException exception) {
            throw new TeacherException(
                String.format("Could not remove Course[id=%s] for Teacher[id=%s]", course, this.getId()),
                exception
            );
        }
    }
}
