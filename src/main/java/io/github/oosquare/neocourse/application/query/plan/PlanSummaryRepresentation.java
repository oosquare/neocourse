package io.github.oosquare.neocourse.application.query.plan;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.infrastructure.repository.plan.PlanSummaryProjection;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PlanSummaryRepresentation {

    private @NonNull String id;
    private @NonNull String name;
    private @NonNull Integer totalClassPeriod;
    private @NonNull Integer requiredClassPeriod;

    public static PlanSummaryRepresentation fromData(@NonNull PlanSummaryProjection data) {
        return PlanSummaryRepresentation.builder()
            .id(data.getId())
            .name(data.getName())
            .totalClassPeriod(data.getTotalClassPeriod())
            .requiredClassPeriod(data.getRequiredClassPeriod())
            .build();
    }
}
