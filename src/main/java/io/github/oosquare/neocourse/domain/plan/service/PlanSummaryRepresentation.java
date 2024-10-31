package io.github.oosquare.neocourse.domain.plan.service;

import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
public class PlanSummaryRepresentation {

    private final @NonNull Id id;
    private final @NonNull PlanName name;
}
