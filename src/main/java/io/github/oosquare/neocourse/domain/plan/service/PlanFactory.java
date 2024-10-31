package io.github.oosquare.neocourse.domain.plan.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.plan.exception.CreatePlanException;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
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
        this.planRepository.findByNameReturningSummary(name)
            .ifPresent(planSummary -> {
                throw new CreatePlanException(
                    String.format(
                        "Plan[id=%s, name=%s] already exists",
                        planSummary.getId(),
                        planSummary.getName()
                    )
                );
            });
    }
}
