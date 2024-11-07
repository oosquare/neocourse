package io.github.oosquare.neocourse.infrastructure.repository.student;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
public class StudentConverter implements DataConverter<Student, StudentData> {

    @Override
    public Student convertToDomain(@NonNull StudentData data) {
        return Student.builder()
            .id(Id.of(data.getId()))
            .username(Username.of(data.getUsername()))
            .displayedUsername(DisplayedUsername.of(data.getDisplayedUsername()))
            .plan(Id.of(data.getPlanId()))
            .transcript(Id.of(data.getTranscriptId()))
            .build();
    }

    @Override
    public StudentData convertToData(@NonNull Student entity) {
        return StudentData.builder()
            .id(entity.getId().getValue())
            .username(entity.getUsername().getValue())
            .displayedUsername(entity.getDisplayedUsername().getValue())
            .planId(entity.getPlan().getValue())
            .transcriptId(entity.getTranscript().getValue())
            .build();
    }
}
