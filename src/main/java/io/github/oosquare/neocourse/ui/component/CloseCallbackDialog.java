package io.github.oosquare.neocourse.ui.component;

import com.vaadin.flow.component.dialog.Dialog;
import lombok.NonNull;

public abstract class CloseCallbackDialog extends Dialog {

    @FunctionalInterface
    public interface CloseEventListener {

        void onCloseEvent();
    }

    private final @NonNull CloseEventListener closeEventListener;

    public CloseCallbackDialog(@NonNull CloseEventListener closeEventListener) {
        this.closeEventListener = closeEventListener;
    }

    @Override
    public void close() {
        super.close();
        this.closeEventListener.onCloseEvent();
    }
}
