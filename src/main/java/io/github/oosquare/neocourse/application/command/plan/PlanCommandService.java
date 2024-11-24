package io.github.oosquare.neocourse.application.command.plan;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.common.service.UserService;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.plan.service.PlanFactory;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;

@Service
@AllArgsConstructor
@Slf4j
public class PlanCommandService {

    private final @NonNull UserService userService;
    private final @NonNull PlanFactory planFactory;
    private final @NonNull PlanRepository planRepository;
    private final @NonNull CourseRepository courseRepository;

    @Transactional
    public void addPlan(@NonNull AddPlanCommand command, @NonNull Account account) {
        log.info("{} requests addPlan with {}", account.toLoggingString(), command);

        var planName = command.getPlanName();

        this.userService.checkIsUser(account, AccountKind.ADMINISTRATOR);
        var plan = this.planFactory.createPlan(planName);
        this.planRepository.save(plan);

        log.info(
            "Added Plan[id={}, name={}] by {}",
            plan.getId(),
            plan.getName(),
            account.toLoggingString()
        );
    }

    @Transactional
    public void includeCourseToPlan(
        @NonNull IncludeCourseCommand command,
        @NonNull Account account
    ) {
        log.info("{} requests includeCourseToPlan with {}", account.toLoggingString(), command);

        var planId = command.getPlanId();
        var courseId = command.getCourseId();

        this.userService.checkIsUser(account, AccountKind.ADMINISTRATOR);
        var plan = this.planRepository.findOrThrow(planId);
        var course = this.courseRepository.findOrThrow(courseId);
        plan.includeCourse(course);
        this.planRepository.save(plan);

        log.info(
            "Included Course[id={}, name={}] to Plan[id={}, name={}] by {}",
            course.getId(),
            course.getName(),
            plan.getId(),
            plan.getName(),
            account.toLoggingString()
        );
    }

    @Transactional
    public void excludeCourseFromPlan(
        @NonNull ExcludeCourseCommand command,
        @NonNull Account account
    ) {
        log.info("{} requests excludeCourseToPlan with {}", account.toLoggingString(), command);

        var planId = command.getPlanId();
        var courseId = command.getCourseId();

        this.userService.checkIsUser(account, AccountKind.ADMINISTRATOR);
        var plan = this.planRepository.findOrThrow(planId);
        var course = this.courseRepository.findOrThrow(courseId);
        plan.excludeCourse(course);
        this.planRepository.save(plan);

        log.info(
            "Excluded Course[id={}, name={}] from Plan[id={}, name={}] by {}",
            course.getId(),
            course.getName(),
            plan.getId(),
            plan.getName(),
            account.toLoggingString()
        );
    }

    @Transactional
    public void assignRequiredClassPeriod(
        @NonNull AssignRequiredClassPeriodCommand command,
        @NonNull Account account
    ) {
        log.info(
            "{} requests AssignRequiredClassPeriodCommand with {}",
            account.toLoggingString(),
            command
        );

        var planId = command.getPlanId();
        var requredClassPeriod = command.getRequiredClassPeriod();

        this.userService.checkIsUser(account, AccountKind.ADMINISTRATOR);
        var plan = this.planRepository.findOrThrow(planId);
        plan.assignRequiredClassPeriod(requredClassPeriod);
        this.planRepository.save(plan);

        log.info(
            "Modified Plan[id={}, name={}, requredClassPeriod={}] by {}",
            plan.getId(),
            plan.getName(),
            plan.getRequiredClassPeriod(),
            account.toLoggingString()
        );
    }
}
