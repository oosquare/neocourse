package io.github.oosquare.neocourse.ui.view.plan;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.plan.PlanCommandService;
import io.github.oosquare.neocourse.application.query.plan.PlanQueryService;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "plans")
public class PlanEditView extends VerticalLayout implements HasUrlParameter<String> {

    private static final @NonNull Account CURRENT_ACCOUNT = Account.builder()
        .id(Id.of("account0"))
        .kind(AccountKind.ADMINISTRATOR)
        .username(Username.of("test-account"))
        .displayedUsername(DisplayedUsername.of("Test Account"))
        .encodedPassword(EncodedPassword.of("password"))
        .user(Id.of("test-user"))
        .build();

    private final @NonNull PlanCommandService planCommandService;
    private final @NonNull PlanQueryService planQueryService;

    private String planId;

    public PlanEditView(
        @NonNull PlanCommandService planCommandService,
        @NonNull PlanQueryService planQueryService
    ) {
        this.planCommandService = planCommandService;
        this.planQueryService = planQueryService;
    }

    @Override
    public void setParameter(BeforeEvent event, String planId) {
        this.planId = planId;
    }
}
