package io.github.oosquare.neocourse.infrastructure.repository.plan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class PlanSummaryProjection {

    private String id;
    private String name;
    private Integer totalClassPeriod;
    private Integer requiredClassPeriod;
}
