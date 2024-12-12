package io.github.oosquare.neocourse.ui.view.user;

import jakarta.annotation.security.RolesAllowed;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.query.account.AccountQueryService;
import io.github.oosquare.neocourse.application.query.account.AccountSummaryRepresentation;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.application.security.Roles;
import io.github.oosquare.neocourse.ui.layout.MainLayout;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users | NeoCourse")
@RolesAllowed(Roles.ADMINISTRATOR)
public class UserListView extends VerticalLayout implements CurrentAccountAwareSupport {

    private final @NonNull AccountQueryService accountQueryService;

    private final @NonNull Grid<AccountSummaryRepresentation> userGrid;

    public UserListView(
        @NonNull AccountQueryService accountQueryService
    ) {
        this.accountQueryService = accountQueryService;

        var title = new H3("Users");

        this.userGrid = this.createUserGrid();

        this.add(title, this.userGrid);
        this.setSizeFull();

        this.updateView();
    }

    private Grid<AccountSummaryRepresentation> createUserGrid() {
        var userGrid = new Grid<>(AccountSummaryRepresentation.class, false);
        userGrid.addColumn(AccountSummaryRepresentation::getUsername)
            .setHeader("Username");
        userGrid.addColumn(AccountSummaryRepresentation::getDisplayedUsername)
            .setHeader("Displayed Username");
        userGrid.addColumn(this.createUserGridEditRender())
            .setHeader("Operation")
            .setAutoWidth(true)
            .setFlexGrow(0);
        userGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        userGrid.setSizeFull();
        return userGrid;
    }

    private LitRenderer<AccountSummaryRepresentation> createUserGridEditRender() {
        var button = "<vaadin-button @click=${handleClick}>Edit</vaadin-button>";
        return LitRenderer.<AccountSummaryRepresentation>of(button)
            .withFunction("handleClick", item -> this.navigateToUserView(item.getId()));
    }

    private void navigateToUserView(String accountId) {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.navigate(UserView.class, accountId));
    }

    public void updateView() {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.access(() -> {
            var account = this.getCurrentAccount();
            var users = this.accountQueryService.getAllAccountsInSummaryRepresentation(account);
            this.userGrid.setItems(users);
        }));
    }
}
