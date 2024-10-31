package io.github.oosquare.neocourse.domain.student.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.User;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor(staticName = "createInternally")
public class Student implements User {

    private final @NonNull Id id;
    private final @NonNull Username username;
    private final @NonNull DisplayedUsername displayedUsername;
    private final @NonNull Id plan;
    private final @NonNull Id transcript;

    public Student(
        @NonNull Id id,
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername,
        @NonNull Plan plan,
        @NonNull Transcript transcript
    ) {
        this.id = id;
        this.username = username;
        this.displayedUsername = displayedUsername;
        this.plan = plan.getId();
        this.transcript = transcript.getId();
    }
}
