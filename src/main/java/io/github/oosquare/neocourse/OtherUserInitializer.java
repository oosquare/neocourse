package io.github.oosquare.neocourse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.application.command.user.SignUpStudentCommand;
import io.github.oosquare.neocourse.application.command.user.SignUpTeacherCommand;
import io.github.oosquare.neocourse.application.command.user.UserCommandService;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanFactory;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
@Profile("development")
@AllArgsConstructor
public class OtherUserInitializer implements CommandLineRunner {

    private final @NonNull PasswordEncoder passwordEncoder;
    private final @NonNull PlanFactory planFactory;
    private final @NonNull PlanRepository planRepository;
    private final @NonNull UserCommandService userCommandService;

    @Override
    @Transactional
    public void run(String... args) {
        this.initializeStudent();
        this.initializeTeacher();
    }

    private Id initializePlan() {
        var plan = this.planFactory.createPlan(PlanName.of("Test Plan"));
        this.planRepository.save(plan);
        return plan.getId();
    }

    private void initializeStudent() {
        var planId = this.initializePlan();
        var password = this.passwordEncoder.encode("password");
        var command = SignUpStudentCommand.builder()
            .username(Username.of("student"))
            .displayedUsername(DisplayedUsername.of("Student"))
            .encodedPassword(EncodedPassword.of(password))
            .planId(planId)
            .build();
        this.userCommandService.signUpStudent(command);
    }

    private void initializeTeacher() {
        var password = this.passwordEncoder.encode("password");
        var command = SignUpTeacherCommand.builder()
            .username(Username.of("teacher"))
            .displayedUsername(DisplayedUsername.of("Teacher"))
            .encodedPassword(EncodedPassword.of(password))
            .build();
        this.userCommandService.signUpTeacher(command);
    }
}
