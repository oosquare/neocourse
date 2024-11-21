package io.github.oosquare.neocourse.application.command.plan;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.plan.model.PlanName;

@Value
@Builder
public class AddPlanCommand {

    private final @NonNull PlanName planName;
}
