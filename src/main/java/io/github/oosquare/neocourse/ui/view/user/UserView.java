package io.github.oosquare.neocourse.ui.view.user;

import jakarta.annotation.security.PermitAll;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.query.account.AccountQueryService;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("User | NeoCourse")
@PermitAll
public class UserView extends VerticalLayout
    implements CurrentAccountAwareSupport, HasUrlParameter<String> {

    public static final String CURRENT_ACCOUNT_USER_PATH = "self";

    private final @NonNull AccountQueryService accountQueryService;
    private String accountId;

    private final @NonNull TextField usernameField;
    private final @NonNull TextField displayedUsernameField;
    private final @NonNull TextField rolesField;

    public UserView(@NonNull AccountQueryService accountQueryService) {
        this.accountQueryService = accountQueryService;

        var title = new H3("User");

        this.usernameField = new TextField("Username");
        this.usernameField.setReadOnly(true);

        this.displayedUsernameField = new TextField("Displayed Username");
        this.displayedUsernameField.setReadOnly(true);

        this.rolesField = new TextField("Role");
        this.rolesField.setReadOnly(true);

        var summaryLayout = new VerticalLayout(
            this.usernameField,
            this.displayedUsernameField,
            this.rolesField
        );
        summaryLayout.setSpacing(false);
        summaryLayout.setPadding(false);

        this.add(title, summaryLayout);
        this.setSizeFull();
    }
    
    @Override
    public void setParameter(BeforeEvent event, String mayBeAccountId) {
        if (CURRENT_ACCOUNT_USER_PATH.equals(mayBeAccountId)) {
            this.accountId = this.getCurrentAccount().getId().getValue();
        } else {
            this.accountId = mayBeAccountId;
        }
        this.updateView();
    }

    private void updateView() {
        var user = this.accountQueryService.getAccountByIdInDetailedRepresentation(
            Id.of(this.accountId),
            this.getCurrentAccount()
        );

        this.usernameField.setValue(user.getUsername());
        this.displayedUsernameField.setValue(user.getDisplayedUsername());
        this.rolesField.setValue(user.getRoles());
    }
}
