package io.github.oosquare.neocourse.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.domain.account.model.AccountRoleKind;
import io.github.oosquare.neocourse.ui.component.LogoutButton;
import io.github.oosquare.neocourse.ui.view.course.CourseListView;
import io.github.oosquare.neocourse.ui.view.index.IndexView;
import io.github.oosquare.neocourse.ui.view.plan.PlanListView;
import io.github.oosquare.neocourse.ui.view.registration.RegistrationListView;
import io.github.oosquare.neocourse.ui.view.schedule.ScheduleListView;
import io.github.oosquare.neocourse.ui.view.transcript.TranscriptView;
import io.github.oosquare.neocourse.ui.view.user.UserListView;

public class MainLayout extends AppLayout implements CurrentAccountAwareSupport {

    public MainLayout() {
        this.setPrimarySection(Section.NAVBAR);

        this.addToNavbar(false, this.createHeader());
        this.addToDrawer(this.createDrawer());
    }

    private Header createHeader() {
        var drawerToggle = new DrawerToggle();
        drawerToggle.setAriaLabel("Menu toggle");
        drawerToggle.setTooltipText("Menu toggle");

        var mainTitle = new H2("NeoCourse");
        mainTitle.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.NONE,
            LumoUtility.Flex.GROW
        );
        var mainTitleWithLink = new RouterLink(IndexView.class);
        mainTitleWithLink.add(mainTitle);
        mainTitleWithLink.getStyle().setTextDecoration("none");
        var mainTitleBlock = new HorizontalLayout(mainTitleWithLink);
        mainTitleBlock.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Flex.GROW);

        var logoutButton = new LogoutButton();
        logoutButton.getStyle().setJustifyContent(Style.JustifyContent.END);

        var header = new Header(drawerToggle, mainTitleBlock, logoutButton);
        header.addClassNames(
            LumoUtility.AlignItems.CENTER,
            LumoUtility.Display.FLEX,
            LumoUtility.Padding.End.MEDIUM,
            LumoUtility.Width.FULL
        );
        return header;
    }

    private Scroller createDrawer() {
        var sideNav = new SideNav();
        var account = this.getCurrentAccount();

        if (account.hasRole(AccountRoleKind.ADMINISTRATOR) || account.hasRole(AccountRoleKind.TEACHER)) {
            sideNav.addItem(new SideNavItem("Courses", CourseListView.class, VaadinIcon.OPEN_BOOK.create()));
        }
        if (account.hasRole(AccountRoleKind.ADMINISTRATOR) || account.hasRole(AccountRoleKind.TEACHER)) {
            sideNav.addItem(new SideNavItem("Plans", PlanListView.class, VaadinIcon.PIN.create()));
        }
        if (account.hasRole(AccountRoleKind.ADMINISTRATOR) || account.hasRole(AccountRoleKind.TEACHER)) {
            sideNav.addItem(new SideNavItem("Schedules", ScheduleListView.class, VaadinIcon.CLOCK.create()));
        }
        if (account.hasRole(AccountRoleKind.ADMINISTRATOR)) {
            sideNav.addItem(new SideNavItem("Users", UserListView.class, VaadinIcon.USER.create()));
        }
        if (account.hasRole(AccountRoleKind.STUDENT)) {
            sideNav.addItem(new SideNavItem("Registrations", RegistrationListView.class, VaadinIcon.PENCIL.create()));
        }
        if (account.hasRole(AccountRoleKind.STUDENT)) {
            sideNav.addItem(new SideNavItem(
                "My Registrations",
                "registrations/" + RegistrationListView.CURRENT_ACCOUNT_REGISTRATION_PATH,
                VaadinIcon.TABLE.create())
            );
        }
        if (account.hasRole(AccountRoleKind.STUDENT)) {
            sideNav.addItem(new SideNavItem(
                "My Transcript",
                "transcripts/" + TranscriptView.CURRENT_ACCOUNT_TRANSCRIPT_PATH,
                VaadinIcon.BAR_CHART.create()
            ));
        }

        var scroller = new Scroller(sideNav);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        return scroller;
    }
}
