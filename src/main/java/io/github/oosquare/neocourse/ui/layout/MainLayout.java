package io.github.oosquare.neocourse.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

import io.github.oosquare.neocourse.ui.view.course.CourseView;

public class MainLayout extends AppLayout {

    public MainLayout() {
        this.setPrimarySection(Section.NAVBAR);

        var drawerToggle = new DrawerToggle();
        drawerToggle.setAriaLabel("Menu toggle");
        drawerToggle.setTooltipText("Menu toggle");

        var mainTitle = new H2("NeoCourse");
        mainTitle.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.NONE,
            LumoUtility.Flex.GROW
        );

        var header = new Header(drawerToggle, mainTitle);
        header.addClassNames(
            LumoUtility.AlignItems.CENTER,
            LumoUtility.Display.FLEX,
            LumoUtility.Padding.End.MEDIUM,
            LumoUtility.Width.FULL
        );

        this.addToNavbar(false, header);

        var sideNav = new SideNav();
        sideNav.addItem(new SideNavItem("Courses", CourseView.class, VaadinIcon.OPEN_BOOK.create()));
        this.addToDrawer(new Scroller(sideNav));
    }
}
