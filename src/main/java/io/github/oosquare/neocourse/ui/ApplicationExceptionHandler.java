package io.github.oosquare.neocourse.ui;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import lombok.extern.slf4j.Slf4j;

import io.github.oosquare.neocourse.utility.exception.NeoCourseException;

@Slf4j
public class ApplicationExceptionHandler implements ErrorHandler {

    @Override
    public void error(ErrorEvent event) {
        switch (event.getThrowable()) {
            case NeoCourseException exception -> this.handleNeoCourseException(exception);
            case Exception exception -> this.handleException(exception);
            default -> {}
        }
    }

    private void handleNeoCourseException(NeoCourseException exception) {
        log.warn("Application error occurred", exception);
        this.showExceptionMessage(exception.getUserMessage());
    }

    private void handleException(Exception exception) {
        log.error("Unknown error occurred", exception);
        this.showExceptionMessage("An unknown error occurred. Please try again later.");
    }

    private void showExceptionMessage(String message) {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.access(() -> {
            var notification = Notification.show(message);
            notification.setPosition(Notification.Position.TOP_END);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }));
    }
}
