package io.github.oosquare.neocourse.ui.view.index;

import jakarta.annotation.security.PermitAll;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.ui.layout.MainLayout;

@Route(value = "", layout = MainLayout.class)
@PageTitle("NeoCourse")
@PermitAll
public class IndexView extends VerticalLayout implements CurrentAccountAwareSupport {

    public IndexView() {
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.setSizeFull();

        var title = new H1("NeoCourse");

        var description = new Div();
        description.setText("Yet another course selection system");
        description.addClassName(LumoUtility.FontSize.XLARGE);
        description.getStyle().setTextAlign(Style.TextAlign.CENTER);

        var welcome = new Div();
        welcome.setText(this.getWelcomeMessage());
        welcome.getStyle().setFont(LumoUtility.TextColor.SECONDARY);
        welcome.getStyle().setTextAlign(Style.TextAlign.CENTER);

        this.add(title, description, welcome);
    }

    private String getWelcomeMessage() {
        var name = this.getCurrentAccount().getDisplayedUsername().getValue();
        return "Welcome, %s!".formatted(name);
    }
}
