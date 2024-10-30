package io.github.oosquare.neocourse.domain.student.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.common.model.User;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor(staticName = "createInternally")
public class Student implements User {

    private final @NonNull Id id;
    private final @NonNull Username username;
    private final @NonNull DisplayedUsername displayedUsername;
    private final @NonNull Id plan;
    private @NonNull PlanScore planScore;

    public Student(
        @NonNull Id id,
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername,
        @NonNull Plan plan
    ) {
        this.id = id;
        this.username = username;
        this.displayedUsername = displayedUsername;
        this.plan = plan.getId();
        this.planScore = new PlanScore();
    }

    public void assignScoreForCourse(@NonNull Course course, @NonNull Score score) {
        this.planScore = this.planScore.assignScoreForCourse(course.getId(), score);
    }

    public FinalResult calculateFinalResult(
        @NonNull Map<Id, ClassPeriod> includedClassPeriods,
        @NonNull ClassPeriod requiredClassPeriod
    ) {
        return this.planScore.calculateFinalResult(
            includedClassPeriods,
            requiredClassPeriod
        );
    }
}
