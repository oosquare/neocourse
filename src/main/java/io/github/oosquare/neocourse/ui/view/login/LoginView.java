package io.github.oosquare.neocourse.ui.view.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import io.github.oosquare.neocourse.ui.view.signup.SignUpView;

@Route("login")
@AnonymousAllowed
public class LoginView extends HorizontalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm;

    public LoginView() {
        this.loginForm = new LoginForm();
        this.loginForm.setAction("login");
        this.loginForm.setForgotPasswordButtonVisible(false);
        // Currently no better way to remove the title
        this.loginForm.getElement().executeJs("""
            document.querySelector("vaadin-login-form-wrapper")
                .shadowRoot
                .querySelector("h2")
                .remove()
        """);

        var signUpLink = new RouterLink("Don't have an account? Sign up here", SignUpView.class);

        var formLayout = new VerticalLayout(this.loginForm, signUpLink);
        formLayout.setWidth("22rem");
        formLayout.setMaxWidth("50%");
        formLayout.setSpacing(false);
        formLayout.setPadding(false);
        formLayout.setAlignItems(Alignment.CENTER);

        var titleLayout = this.createTitleLayout();

        this.add(titleLayout, formLayout);

        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private VerticalLayout createTitleLayout() {
        var title = new H1("NeoCourse");
        var subtitle = new H2("Log in");
        var layout = new VerticalLayout(title, subtitle);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setWidth("22rem");
        layout.setMaxWidth("50%");
        return layout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
