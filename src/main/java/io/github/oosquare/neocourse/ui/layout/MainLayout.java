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

import io.github.oosquare.neocourse.ui.view.course.CourseListView;
import io.github.oosquare.neocourse.ui.view.plan.PlanListView;
import io.github.oosquare.neocourse.ui.view.schedule.ScheduleListView;

public class MainLayout extends AppLayout {

    public MainLayout() {
        this.setPrimarySection(Section.NAVBAR);

        var header = createHeader();
        this.addToNavbar(false, header);

        var scroller = createDrawer();
        this.addToDrawer(scroller);
    }

    private static Header createHeader() {
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
        return header;
    }

    private static Scroller createDrawer() {
        var sideNav = new SideNav();
        sideNav.addItem(new SideNavItem("Courses", CourseListView.class, VaadinIcon.OPEN_BOOK.create()));
        sideNav.addItem(new SideNavItem("Plans", PlanListView.class, VaadinIcon.PIN.create()));
        sideNav.addItem(new SideNavItem("Schedules", ScheduleListView.class, VaadinIcon.CLOCK.create()));

        var scroller = new Scroller(sideNav);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        return scroller;
    }
}
