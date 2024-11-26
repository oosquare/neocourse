package io.github.oosquare.neocourse.application.query.schedule;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
@Builder
public class ByStudentQuery {

    private final @NonNull Id studentId;
}
