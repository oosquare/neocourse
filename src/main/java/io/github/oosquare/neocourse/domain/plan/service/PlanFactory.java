package io.github.oosquare.neocourse.domain.plan.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.utility.exception.FieldDuplicationException;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

@Service
@AllArgsConstructor
public class PlanFactory {

    private final @NonNull IdGenerator idGenerator;
    private final @NonNull PlanRepository planRepository;

    public Plan createPlan(@NonNull PlanName name) {
        this.checkPlanNotDuplicated(name);
        var id = this.idGenerator.generate();
        return new Plan(id, name);
    }

    private void checkPlanNotDuplicated(PlanName name) {
        this.planRepository.findByName(name).ifPresent(plan -> {
            throw FieldDuplicationException.builder()
                .message("PlanName is duplicated and conflicted with another plan's")
                .userMessage("Plan's name is duplicated since a plan with the same name already exists")
                .context("name", name)
                .context("plan.id", plan.getId())
                .build();
        });
    }
}
