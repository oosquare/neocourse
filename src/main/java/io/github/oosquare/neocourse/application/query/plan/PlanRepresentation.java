package io.github.oosquare.neocourse.application.query.plan;

import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.infrastructure.repository.plan.PlanData;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PlanRepresentation {

    private @NonNull String id;
    private @NonNull String planName;
    private @NonNull Integer totalClassPeriod;
    private @NonNull Integer requiredClassPeriod;
    private @NonNull Set<String> includedCourseIds;

    public static PlanRepresentation fromData(@NonNull PlanData data) {
        return PlanRepresentation.builder()
            .id(data.getId())
            .planName(data.getName())
            .totalClassPeriod(data.getTotalClassPeriod())
            .requiredClassPeriod(data.getRequiredClassPeriod())
            .includedCourseIds(data.getIncludedCourseIds())
            .build();
    }
}