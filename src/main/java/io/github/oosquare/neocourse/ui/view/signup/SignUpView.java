package io.github.oosquare.neocourse.ui.view.signup;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.Data;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.oosquare.neocourse.application.command.user.SignUpStudentCommand;
import io.github.oosquare.neocourse.application.command.user.SignUpTeacherCommand;
import io.github.oosquare.neocourse.application.command.user.UserCommandService;
import io.github.oosquare.neocourse.application.query.plan.PlanQueryService;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.ui.component.StudentSignUpArea;
import io.github.oosquare.neocourse.ui.component.TeacherSignUpArea;
import io.github.oosquare.neocourse.ui.view.login.LoginView;
import io.github.oosquare.neocourse.utility.id.Id;

@Route("signup")
@PageTitle("Sign up | NeoCourse")
@AnonymousAllowed
public class SignUpView extends HorizontalLayout {

    private static final Logger log = LoggerFactory.getLogger(SignUpView.class);

    @Data
    private static class TeacherSignUpEditModel {

        private String username;
        private String displayedUsername;
        private String password;
    }

    private static final String TAB_LABEL_STUDENT = "Student";
    private static final String TAB_LABEL_TEACHER = "Teacher";

    private final @NonNull UserCommandService userCommandService;
    private final @NonNull PlanQueryService planQueryService;
    private final @NonNull PasswordEncoder passwordEncoder;

    private final @NonNull StudentSignUpArea studentSignUpArea;
    private final @NonNull TeacherSignUpArea teacherSignUpArea;
    private final @NonNull VerticalLayout tabContainer;

    public SignUpView(
        @NonNull UserCommandService userCommandService,
        @NonNull PlanQueryService planQueryService,
        @NonNull PasswordEncoder passwordEncoder
    ) {
        this.userCommandService = userCommandService;
        this.planQueryService = planQueryService;
        this.passwordEncoder = passwordEncoder;

        var titleLayout = this.createTitleLayout();

        var signUpTabs = new Tabs(new Tab(TAB_LABEL_STUDENT), new Tab(TAB_LABEL_TEACHER));
        signUpTabs.setWidthFull();
        signUpTabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        signUpTabs.addSelectedChangeListener(event -> {
            this.switchTab(event.getSelectedTab().getLabel());
        });

        this.studentSignUpArea = new StudentSignUpArea(this.planQueryService, this::signUpStudent);
        this.teacherSignUpArea = new TeacherSignUpArea(this::signUpTeacher);

        this.tabContainer = new VerticalLayout(this.studentSignUpArea);
        this.tabContainer.setPadding(false);

        var loginLink = new RouterLink("Already have an account? Log in here", LoginView.class);
        loginLink.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        var formLayout = new VerticalLayout(signUpTabs, this.tabContainer, loginLink);

        formLayout.setWidth("22rem");

        this.add(titleLayout, formLayout);

        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.getStyle().setFlexWrap(Style.FlexWrap.WRAP);
    }

    private VerticalLayout createTitleLayout() {
        var title = new H1("NeoCourse");
        var subtitle = new H2("Sign up");
        var layout = new VerticalLayout(title, subtitle);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setWidth("22rem");
        return layout;
    }

    private void switchTab(String label) {
        this.tabContainer.removeAll();
        if (label.equals(TAB_LABEL_STUDENT)) {
            this.tabContainer.add(this.studentSignUpArea);
        } else {
            this.tabContainer.add(this.teacherSignUpArea);
        }
    }

    private void signUpStudent(StudentSignUpArea.StudentSignUpEditModel model) {
        var password = this.passwordEncoder.encode(model.getPassword());
        var command = SignUpStudentCommand.builder()
            .username(Username.of(model.getUsername()))
            .displayedUsername(DisplayedUsername.of(model.getDisplayedUsername()))
            .encodedPassword(EncodedPassword.of(password))
            .planId(Id.of(model.getPlanId()))
            .build();
        this.userCommandService.signUpStudent(command);
        this.showSuccessMessage();
        this.navigateToLogin();
    }
    
    private void signUpTeacher(TeacherSignUpArea.TeacherSignUpEditModel model) {
        var password = this.passwordEncoder.encode(model.getPassword());
        var command = SignUpTeacherCommand.builder()
            .username(Username.of(model.getUsername()))
            .displayedUsername(DisplayedUsername.of(model.getDisplayedUsername()))
            .encodedPassword(EncodedPassword.of(password))
            .build();
        this.userCommandService.signUpTeacher(command);
        this.showSuccessMessage();
        this.navigateToLogin();
    }

    private void showSuccessMessage() {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.access(() -> {
            var notification = Notification.show("You have successfully signed up");
            notification.setPosition(Notification.Position.TOP_END);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }));
    }

    private void navigateToLogin() {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.navigate(LoginView.class));
    }
}
