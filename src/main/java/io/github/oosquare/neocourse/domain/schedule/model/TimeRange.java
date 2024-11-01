package io.github.oosquare.neocourse.domain.schedule.model;

import java.time.Duration;
import java.time.ZonedDateTime;

import lombok.NonNull;
import lombok.Value;

@Value
public class TimeRange {

    private final @NonNull ZonedDateTime start;
    private final @NonNull Duration period;

    private TimeRange(@NonNull ZonedDateTime start, @NonNull Duration period) {
        this.start = start;
        this.period = period;
    }

    public static TimeRange of(@NonNull ZonedDateTime start, @NonNull Duration period) {
        return new TimeRange(start, period);
    }

    public static TimeRange ofInternally(@NonNull ZonedDateTime start, @NonNull Duration period) {
        return TimeRange.of(start, period);
    }

    public boolean hasOverlap(@NonNull TimeRange other) {
        return this.start.isBefore(other.start.plus(other.period))
            && other.start.isBefore(this.start.plus(this.period));
    }
}
