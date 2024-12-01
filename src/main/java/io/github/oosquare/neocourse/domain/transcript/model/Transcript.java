package io.github.oosquare.neocourse.domain.transcript.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.Entity;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Transcript implements Entity {

    public static final Score DEFAULT_SCORE_FOR_UNEVALUATED = Score.of(60);

    private final @NonNull Id id;
    private final @NonNull Id plan;
    private final @NonNull Map<Id, TranscriptItem> courseScores;

    public Transcript(@NonNull Id id, @NonNull Plan plan) {
        this(id, plan.getId(), new HashMap<>());
    }

    public void addCourse(@NonNull Course course) {
        this.courseScores.computeIfAbsent(
            course.getId(),
            key -> TranscriptItem.of(course.getId(), course.getClassPeriod())
        );
    }

    public void removeCourseIfNotGraded(@NonNull Course course) {
        this.courseScores.computeIfPresent(
            course.getId(),
            (key, item) -> (item.isEvaluated() ? item : null)
        );
    }

    public void gradeCourse(@NonNull Course course, @NonNull Score score) {
        this.checkIncludeCourse(course);
        this.courseScores.computeIfPresent(
            course.getId(),
            (key, item) -> item.assignScore(score)
        );
    }

    public void markAbsent(@NonNull Course course) {
        this.checkIncludeCourse(course);
        this.courseScores.computeIfPresent(
            course.getId(),
            (key, item) -> item.markAbsent()
        );
    }

    private void checkIncludeCourse(Course course) {
        if (!this.courseScores.containsKey(course.getId())) {
            throw RuleViolationException.builder()
                .message("Transcript doesn't include this Course")
                .userMessage("Transcript doesn't include this course")
                .context("transcript.id", this.getId())
                .context("course.id", course.getId())
                .context("course.name", course.getName())
                .build();
        }
    }

    public boolean isCourseSelectable(@NonNull Course course) {
        return Optional.ofNullable(this.courseScores.get(course.getId()))
            .map(item -> !item.accountsForEstimatedClassPeriod())
            .orElse(true);
    }

    public Optional<Score> getScore(@NonNull Course course) {
        return Optional.ofNullable(this.courseScores.get(course.getId()))
            .flatMap(TranscriptItem::getScore);
    }

    public Optional<ClassPeriod> calculateEstimatedClassPeriod() {
        return this.courseScores.values()
            .stream()
            .filter(TranscriptItem::accountsForEstimatedClassPeriod)
            .map(TranscriptItem::getClassPeriod)
            .reduce((sum, item) -> ClassPeriod.of(sum.getValue() + item.getValue()));
    }

    public FinalResult calculateFinalResult(@NonNull ClassPeriod requiredClassPeriod) {
        int estimatedClassPeriod = 0;
        double weightedScoreSum = 0;

        for (var item : this.courseScores.values()) {
            if (item.accountsForEstimatedClassPeriod()) {
                estimatedClassPeriod += item.getClassPeriod().getValue();
                var score = item.getScore().orElse(Transcript.DEFAULT_SCORE_FOR_UNEVALUATED);
                weightedScoreSum += score.getValue() * item.getClassPeriod().getValue();
            }
        }

        if (estimatedClassPeriod < requiredClassPeriod.getValue()) {
            return FinalResult.ofPlanUnfinished();
        } else {
            double averageScore = estimatedClassPeriod != 0
                ? weightedScoreSum / estimatedClassPeriod
                : 0.0;
            return FinalResult.ofPlanFinished(averageScore);
        }
    }
}
