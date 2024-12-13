package io.github.oosquare.neocourse.ui.view.user;

import jakarta.annotation.security.PermitAll;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.oosquare.neocourse.application.command.user.UpgradeToAdministratorCommand;
import io.github.oosquare.neocourse.application.command.user.UserCommandService;
import io.github.oosquare.neocourse.application.query.account.AccountQueryService;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.ui.view.index.IndexView;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("User | NeoCourse")
@PermitAll
public class UserView extends VerticalLayout
    implements CurrentAccountAwareSupport, HasUrlParameter<String> {

    public static final String CURRENT_ACCOUNT_USER_PATH = "self";

    private final @NonNull PasswordEncoder passwordEncoder;
    private final @NonNull UserCommandService userCommandService;
    private final @NonNull AccountQueryService accountQueryService;
    private String accountId;

    private final @NonNull TextField usernameField;
    private final @NonNull TextField displayedUsernameField;
    private final @NonNull TextField rolesField;

    public UserView(
        @NonNull PasswordEncoder passwordEncoder,
        @NonNull UserCommandService userCommandService,
        @NonNull AccountQueryService accountQueryService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userCommandService = userCommandService;
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
        this.add(this.createButtonLayout());
        this.updateView();
    }

    private HorizontalLayout createButtonLayout() {
        var layout = new HorizontalLayout();

        if (this.getCurrentAccount().getId().getValue().equals(this.accountId)) {
            layout.add(this.createChangePasswordButton());
        }

        if (this.getCurrentAccount().hasRole(AccountRoleKind.ADMINISTRATOR)) {
            layout.add(this.createUpgradeToAdministratorButton());
        }

        var returnButton = new Button("Return");
        returnButton.addClickListener(event -> this.navigateBack());
        layout.add(returnButton);

        return layout;
    }

    private Button createChangePasswordButton() {
        return new Button("Change Password", event -> {
            var dialog = new UserChangePasswordDialog(
                this.userCommandService,
                this.passwordEncoder,
                this::updateView
            );
            dialog.open();
        });
    }

    private Button createUpgradeToAdministratorButton() {
        var dialog = new ConfirmDialog();
        dialog.setHeader("Upgrade To Administrator");
        dialog.setText("Do you want to make this user an administrator?");
        dialog.setCancelable(true);
        dialog.setRejectable(false);
        dialog.setConfirmText("Upgrade");
        dialog.addConfirmListener(event -> this.upgradeAdministrator());

        return new Button("Upgrade To Administrator", event -> {
            dialog.open();
        });
    }

    private void upgradeAdministrator() {
        var command = UpgradeToAdministratorCommand.builder()
            .accountId(Id.of(this.accountId))
            .build();
        var account = this.getCurrentAccount();
        this.userCommandService.upgradeToAdministrator(command, account);

        this.updateView();
    }

    private void navigateBack() {
        if (this.getCurrentAccount().hasRole(AccountRoleKind.ADMINISTRATOR)) {
            Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.navigate(UserListView.class));
        } else {
            Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.navigate(IndexView.class));
        }
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
