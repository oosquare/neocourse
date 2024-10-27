package io.github.oosquare.neocourse.domain.model.student;

import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.model.common.Username;
import io.github.oosquare.neocourse.domain.model.common.User;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
public class Student extends User {

    private final @NonNull Id coursePlan;
    private @NonNull CoursePlanScore coursePlanScore;

    public Student(@NonNull Id id, @NonNull Username username, @NonNull Id coursePlan) {
        super(id, username);
        this.coursePlan = coursePlan;
        this.coursePlanScore = new CoursePlanScore();
    }
}
