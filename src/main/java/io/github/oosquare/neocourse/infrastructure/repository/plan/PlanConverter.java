package io.github.oosquare.neocourse.infrastructure.repository.plan;

import java.util.HashSet;
import java.util.stream.Collectors;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.plan.model.CourseSet;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
public class PlanConverter implements DataConverter<Plan, PlanData> {

    @Override
    public Plan convertToDomain(@NonNull PlanData data) {
        return Plan.builder()
            .id(Id.of(data.getId()))
            .name(PlanName.of(data.getName()))
            .requiredClassPeriod(ClassPeriod.of(data.getRequiredClassPeriod()))
            .includedCourses(CourseSet.ofInternally(data.getIncludedCourseIds()
                .stream()
                .map(Id::of)
                .collect(Collectors.toCollection(HashSet::new))))
            .build();
    }

    @Override
    public PlanData convertToData(@NonNull Plan entity) {
        return PlanData.builder()
            .id(entity.getId().getValue())
            .name(entity.getName().getValue())
            .requiredClassPeriod(entity.getRequiredClassPeriod().getValue())
            .includedCourseIds(entity.getIncludedCourses()
                .getCourses()
                .stream()
                .map(Id::getValue)
                .collect(Collectors.toSet()))
            .build();
    }
}
