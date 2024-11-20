package io.github.oosquare.neocourse.infrastructure.repository.teacher;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
public class TeacherConverter implements DataConverter<Teacher, TeacherData> {

    @Override
    public Teacher convertToDomain(@NonNull TeacherData data) {
        return Teacher.builder()
            .id(Id.of(data.getId()))
            .username(Username.of(data.getUsername()))
            .displayedUsername(DisplayedUsername.of(data.getDisplayedUsername()))
            .build();
    }

    @Override
    public TeacherData convertToData(@NonNull Teacher entity) {
        return TeacherData.builder()
            .id(entity.getId().getValue())
            .username(entity.getUsername().getValue())
            .displayedUsername(entity.getDisplayedUsername().getValue())
            .build();
    }
}
