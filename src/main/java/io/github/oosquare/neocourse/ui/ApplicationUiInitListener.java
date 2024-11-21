package io.github.oosquare.neocourse.ui;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUiInitListener
    implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(e -> {
            e.getSession().setErrorHandler(new ApplicationExceptionHandler());
        });
    }
}
