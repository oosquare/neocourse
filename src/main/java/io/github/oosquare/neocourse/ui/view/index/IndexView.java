package io.github.oosquare.neocourse.ui.view.index;

import jakarta.annotation.security.PermitAll;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import io.github.oosquare.neocourse.ui.layout.MainLayout;

@Route(value = "", layout = MainLayout.class)
@PermitAll
public class IndexView extends VerticalLayout {

    public IndexView() {
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.setSizeFull();

        var title = new H1("NeoCourse");

        var content = new Div();
        content.setText("Yet another course selection system");
        content.addClassName(LumoUtility.FontSize.XLARGE);

        this.add(title, content);
    }
}
