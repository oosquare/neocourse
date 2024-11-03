package io.github.oosquare.neocourse.domain.transcript.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.Entity;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.transcript.exception.TranscriptException;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor(staticName = "createInternally")
public class Transcript implements Entity {

    private static final Score DEFAULT_SCORE_FOR_UNEVALUATED = Score.of(60);

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
        if (!this.courseScores.containsKey(course.getId())) {
            throw new TranscriptException(String.format(
                "Transcript[id=%s] doesn't include Course[id=%s]",
                this.getId(),
                course.getName()
            ));
        }
        this.courseScores.computeIfPresent(
            course.getId(),
            (key, item) -> item.assignScore(score)
        );
    }

    public boolean isCourseSelectable(@NonNull Course course) {
        return Optional.ofNullable(this.courseScores.get(course.getId()))
            .map(item -> !item.accountsForEstimatedClassPeriod())
            .orElse(true);
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
            double averageScore = weightedScoreSum / estimatedClassPeriod;
            return FinalResult.ofPlanFinished(averageScore);
        }
    }
}
