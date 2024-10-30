package io.github.oosquare.neocourse.domain.common.model;

import io.github.oosquare.neocourse.domain.Entity;

public interface User extends Entity {

    Username getUsername();

    DisplayedUsername getDisplayedUsername();
}
