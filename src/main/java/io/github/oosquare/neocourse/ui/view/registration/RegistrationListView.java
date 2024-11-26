package io.github.oosquare.neocourse.ui.view.registration;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.registration.AddRegistrationCommand;
import io.github.oosquare.neocourse.application.command.registration.CancelRegistrationCommand;
import io.github.oosquare.neocourse.application.command.registration.RegistrationCommandService;
import io.github.oosquare.neocourse.application.query.schedule.ScheduleQueryService;
import io.github.oosquare.neocourse.application.query.schedule.ScheduleSummaryRepresentation;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.ui.component.ScheduleGrid;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "registrations", layout = MainLayout.class)
public class RegistrationListView extends VerticalLayout {

    private static final @NonNull Account CURRENT_ACCOUNT = Account.builder()
        .id(Id.of("account2"))
        .kind(AccountKind.STUDENT)
        .username(Username.of("test-student"))
        .displayedUsername(DisplayedUsername.of("Test Student"))
        .encodedPassword(EncodedPassword.of("password"))
        .user(Id.of("test-student"))
        .build();

    private final @NonNull RegistrationCommandService registrationCommandService;
    private final @NonNull ScheduleQueryService scheduleQueryService;

    private final @NonNull Grid<ScheduleSummaryRepresentation> scheduleGrid;

    public RegistrationListView(
        @NonNull RegistrationCommandService registrationCommandService,
        @NonNull ScheduleQueryService scheduleQueryService
    ) {
        this.registrationCommandService = registrationCommandService;
        this.scheduleQueryService = scheduleQueryService;

        this.scheduleGrid = this.createScheduleGrid();

        this.add(this.scheduleGrid);
        this.setSizeFull();

        this.updateView();
    }

    private Grid<ScheduleSummaryRepresentation> createScheduleGrid() {
        var scheduleGrid = new ScheduleGrid();
        scheduleGrid.addColumn(this.createPlanGridOperationRender())
            .setHeader("Operation")
            .setAutoWidth(true)
            .setFlexGrow(0);
        scheduleGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        scheduleGrid.setSizeFull();
        return scheduleGrid;
    }

    private LitRenderer<ScheduleSummaryRepresentation> createPlanGridOperationRender() {
        var buttons = """
            <div>
                <vaadin-button @click=${handleRegisterClick}>Register</vaadin-button>
                <vaadin-button @click=${handleCancelClick}>Cancel</vaadin-button>
            </div>
        """;
        return LitRenderer.<ScheduleSummaryRepresentation>of(buttons)
            .withFunction("handleRegisterClick", this::addRegistration)
            .withFunction("handleCancelClick", this::cancelRegistration);
    }

    private void addRegistration(ScheduleSummaryRepresentation schedule) {
        var command = AddRegistrationCommand.builder()
            .scheduleId(Id.of(schedule.getId()))
            .build();
        var account = this.getCurrentAccount();
        this.registrationCommandService.addRegistration(command, account);

        this.showSuccessMessage("Successfully registered for schedule of %s"
            .formatted(schedule.getCourseName()));
    }

    private void cancelRegistration(ScheduleSummaryRepresentation schedule) {
        var command = CancelRegistrationCommand.builder()
            .scheduleId(Id.of(schedule.getId()))
            .build();
        var account = this.getCurrentAccount();
        this.registrationCommandService.cancelRegistration(command, account);

        this.showSuccessMessage("Successfully cancelled registration for schedule of %s"
            .formatted(schedule.getCourseName()));
    }

    private void showSuccessMessage(String message) {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.access(() -> {
            var notification = Notification.show(message);
            notification.setPosition(Notification.Position.TOP_END);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }));
    }

    private void updateView() {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.access(() -> {
            var account = this.getCurrentAccount();
            var schedules = this.scheduleQueryService.getAllSchedulesInSummaryRepresentation(account);
            this.scheduleGrid.setItems(schedules);
        }));
    }

    private Account getCurrentAccount() {
        return CURRENT_ACCOUNT;
    }
}
