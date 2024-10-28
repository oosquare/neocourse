package io.github.oosquare.neocourse.domain.student.model;

import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.common.model.User;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
public class Student extends User {

    private final @NonNull Id coursePlan;
    private @NonNull CoursePlanScore coursePlanScore;

    public Student(
        @NonNull Id id,
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername,
        @NonNull Id coursePlan
    ) {
        super(id, username, displayedUsername);
        this.coursePlan = coursePlan;
        this.coursePlanScore = new CoursePlanScore();
    }
}
