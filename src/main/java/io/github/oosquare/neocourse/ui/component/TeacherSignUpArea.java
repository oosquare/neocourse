package io.github.oosquare.neocourse.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import lombok.Data;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.query.plan.PlanQueryService;

public class TeacherSignUpArea extends VerticalLayout {

    @Data
    public static class TeacherSignUpEditModel {

        private String username;
        private String displayedUsername;
        private String password;
    }

    @FunctionalInterface
    public interface TeacherSignUpListener {

        void signUp(TeacherSignUpEditModel model);
    }

    private final @NonNull TeacherSignUpListener teacherSignUpListener;

    public TeacherSignUpArea(@NonNull TeacherSignUpListener teacherSignUpListener) {
        this.teacherSignUpListener = teacherSignUpListener;

        var usernameField = new TextField("Username");
        var displayedUsernameField = new TextField("Displayed Username");
        var passwordField = new PasswordField("Password");

        var fieldsLayout = new VerticalLayout(
            usernameField,
            displayedUsernameField,
            passwordField
        );
        fieldsLayout.setSpacing(false);
        fieldsLayout.setPadding(false);
        fieldsLayout.setAlignItems(Alignment.STRETCH);

        var binder = new Binder<TeacherSignUpEditModel>();
        binder.forField(usernameField)
            .asRequired()
            .bind(TeacherSignUpEditModel::getUsername, TeacherSignUpEditModel::setUsername);
        binder.forField(displayedUsernameField)
            .asRequired()
            .bind(TeacherSignUpEditModel::getDisplayedUsername, TeacherSignUpEditModel::setDisplayedUsername);
        binder.forField(passwordField)
            .asRequired()
            .bind(TeacherSignUpEditModel::getPassword, TeacherSignUpEditModel::setPassword);

        var signUpButton = this.createTeacherSignUpButton(binder);

        this.add(fieldsLayout, signUpButton);
        this.setPadding(false);
        this.setAlignItems(Alignment.STRETCH);
    }

    private Button createTeacherSignUpButton(Binder<TeacherSignUpEditModel> binder) {
        var signUpButton = new Button("Sign up");
        signUpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signUpButton.addClickListener(event -> {
            try {
                signUpButton.setEnabled(false);
                var model = new TeacherSignUpEditModel();
                binder.writeBean(model);
                this.teacherSignUpListener.signUp(model);
            } catch (ValidationException ignored) {
                // Error message is already shown in UI. Nothing needed here.
            } finally {
                signUpButton.setEnabled(true);
            }
        });
        return signUpButton;
    }
}
