package io.github.oosquare.neocourse.domain.plan.model;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(includeFieldNames = false)
public class PlanName {

    private final @NonNull String value;

    private PlanName(String value) {
        Preconditions.checkArgument(!value.isBlank(), "Plan name should not be blank");
        Preconditions.checkArgument(
            value.strip().equals(value),
            "Plan name should not have leading and trailing spaces"
        );
        this.value = value;
    }

    public static PlanName of(@NonNull String value) {
        return new PlanName(value);
    }

    public static PlanName ofInternally(@NonNull String value) {
        return PlanName.of(value);
    }
}
