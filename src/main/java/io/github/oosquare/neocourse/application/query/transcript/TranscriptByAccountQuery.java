package io.github.oosquare.neocourse.application.query.transcript;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
@Builder
public class TranscriptByAccountQuery {

    private final @NonNull Id accountId;
}
