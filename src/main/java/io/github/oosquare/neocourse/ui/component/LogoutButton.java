package io.github.oosquare.neocourse.ui.component;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class LogoutButton extends Button {

    public static final String LOGOUT_SUCCESS_URL = "/login";

    public LogoutButton() {
        super("Log out");
        this.addClickListener(event -> {
            Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> {
                ui.getPage().setLocation(LOGOUT_SUCCESS_URL);
            });
            var handler = new SecurityContextLogoutHandler();
            handler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
        });
    }
}
