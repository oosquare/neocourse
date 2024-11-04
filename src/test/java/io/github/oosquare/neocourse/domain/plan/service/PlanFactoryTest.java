package io.github.oosquare.neocourse.domain.plan.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.plan.exception.CreatePlanException;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanFactoryTest {

    private @Mock IdGenerator idGenerator;
    private @Mock PlanRepository planRepository;
    private @InjectMocks PlanFactory planFactory;

    @Test
    void createPlanSucceeds() {
        when(this.idGenerator.generate())
            .thenReturn(Id.of("0"));
        when(this.planRepository.findByName(any()))
            .thenReturn(Optional.empty());

        var plan = this.planFactory.createPlan(PlanName.of("test plan"));
        assertEquals(Id.of("0"), plan.getId());
        assertEquals(PlanName.of("test plan"), plan.getName());
        assertTrue(plan.getIncludedCourses().getCourses().isEmpty());
    }

    @Test
    void createPlanThrowsWhenPlanNameIsDuplicated() {
        when(this.planRepository.findByName(PlanName.of("test plan")))
            .thenReturn(Optional.of(new Plan(
                Id.of("0"),
                PlanName.of("test plan")
            )));

        assertThrows(CreatePlanException.class, () -> {
            this.planFactory.createPlan(PlanName.of("test plan"));
        });
    }
}