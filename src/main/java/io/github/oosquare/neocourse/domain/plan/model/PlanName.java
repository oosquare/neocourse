package io.github.oosquare.neocourse.domain.plan.model;

import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class PlanName {

    private final @NonNull String courseName;

    public PlanName(@NonNull String planName) {
        checkArgument(!planName.isBlank(), "Plan name should not be blank");
        checkArgument(
            planName.strip().equals(planName),
            "Plan name should not have leading and trailing spaces"
        );
        this.courseName = planName;
    }
}
