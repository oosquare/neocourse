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
            (key, item) -> (item.isGraded() ? item : null)
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

    public Optional<ClassPeriod> calculateEarnedClassPeriod() {
        return this.courseScores.values()
            .stream()
            .map(TranscriptItem::getClassPeriod)
            .reduce((sum, item) -> ClassPeriod.of(sum.getValue() + item.getValue()));
    }

    public FinalResult calculateFinalResult(@NonNull ClassPeriod requiredClassPeriod) {
        int earnedClassPeriod = 0;
        double weightedScoreSum = 0;

        for (var item : this.courseScores.values()) {
            earnedClassPeriod += item.getClassPeriod().getValue();
            weightedScoreSum += item.getScore().getValue() * item.getClassPeriod().getValue();
        }

        if (earnedClassPeriod < requiredClassPeriod.getValue()) {
            return FinalResult.ofPlanUnfinished();
        } else {
            double averageScore = weightedScoreSum / earnedClassPeriod;
            return FinalResult.ofPlanFinished(averageScore);
        }
    }
}
