package io.github.oosquare.neocourse.ui.view.user;

import java.util.Objects;
import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import lombok.Data;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.oosquare.neocourse.application.command.user.ChangePasswordCommand;
import io.github.oosquare.neocourse.application.command.user.UserCommandService;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.ui.component.CloseCallbackDialog;

public class UserChangePasswordDialog extends CloseCallbackDialog implements CurrentAccountAwareSupport {

    @Data
    private static class PasswordEditModel {

        private String oldPassword;
        private String newPassword;
        private String confirmPassword;
    }

    private final @NonNull UserCommandService userCommandService;
    private final @NonNull PasswordEncoder passwordEncoder;

    private final Button changeButton;
    private final PasswordField newPasswordField;
    private final Binder<PasswordEditModel> binder;

    public UserChangePasswordDialog(
        @NonNull UserCommandService userCommandService,
        @NonNull PasswordEncoder passwordEncoder,
        @NonNull CloseEventListener closeEventListener
    ) {
        super(closeEventListener);
        this.userCommandService = userCommandService;
        this.passwordEncoder = passwordEncoder;

        this.setHeaderTitle("Change Password");

        var oldPasswordField = new PasswordField("Old Password");
        var newPasswordField = new PasswordField("New Password");
        var confirmPasswordField = new PasswordField("Confirm Password");
        this.newPasswordField = newPasswordField;

        var dialogLayout = this.createDialogLayout(oldPasswordField, newPasswordField, confirmPasswordField);
        this.add(dialogLayout);

        var cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());
        this.getFooter().add(cancelButton);

        var applyButton = new Button("Apply");
        applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        applyButton.addClickListener(event -> this.applyButtonOnClickEvent());
        this.getFooter().add(applyButton);
        this.changeButton = applyButton;

        this.binder = this.createBinder(oldPasswordField, newPasswordField, confirmPasswordField);
    }

    private VerticalLayout createDialogLayout(
        PasswordField oldPasswordField,
        PasswordField newPasswordField,
        PasswordField confirmPasswordField
    ) {
        var layout = new VerticalLayout(oldPasswordField, newPasswordField, confirmPasswordField);
        layout.setPadding(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.getStyle().set("width", "18rem").set("max-width", "100%");
        return layout;
    }

    private Binder<PasswordEditModel> createBinder(
        PasswordField oldPasswordField,
        PasswordField newPasswordField,
        PasswordField confirmPasswordField
    ) {
        var binder = new Binder<>(PasswordEditModel.class);
        binder.forField(oldPasswordField)
            .asRequired()
            .bind(PasswordEditModel::getOldPassword, PasswordEditModel::setOldPassword);
        binder.forField(newPasswordField)
            .asRequired()
            .bind(PasswordEditModel::getNewPassword, PasswordEditModel::setNewPassword);
        binder.forField(confirmPasswordField)
            .asRequired()
            .withValidator(
                password -> Objects.equals(password, this.newPasswordField.getValue()),
                "New passwords are not identical"
            )
            .bind(PasswordEditModel::getConfirmPassword, PasswordEditModel::setConfirmPassword);
        return binder;
    }

    private void applyButtonOnClickEvent() {
        try {
            this.changeButton.setEnabled(false);

            var model = new PasswordEditModel();
            this.binder.writeBean(model);

            this.changePassword(model);

            this.close();
        } catch (ValidationException ignored) {
            // Error message is already shown in UI. Nothing needed here.
        } finally {
            this.changeButton.setEnabled(true);
        }
    }

    private void changePassword(PasswordEditModel model) {
        var newPassword = EncodedPassword.of(this.passwordEncoder.encode(model.getNewPassword()));
        var command = ChangePasswordCommand.builder()
            .rawOldPassword(model.oldPassword)
            .encodedNewPassword(newPassword)
            .build();
        var account = this.getCurrentAccount();
        this.userCommandService.changePassword(command, account);

        this.showSuccessMessage("Password successfully changed");
    }

    private void showSuccessMessage(String message) {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.access(() -> {
            var notification = Notification.show(message);
            notification.setPosition(Notification.Position.TOP_END);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }));
    }
}
