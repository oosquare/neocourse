package io.github.oosquare.neocourse.infrastructure.repository.teacher;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.id.Id;

@Repository
@AllArgsConstructor
public class TeacherConcreteRepository implements TeacherRepository {

    private final @NonNull TeacherMapper teacherMapper;
    private final @NonNull TeacherConverter teacherConverter;

    @Override
    public Optional<Teacher> findByUsername(@NonNull Username username) {
        return this.teacherMapper.findByUsername(username.getValue())
            .map(this.teacherConverter::convertToDomain);
    }

    @Override
    public Optional<Teacher> find(@NonNull Id id) {
        return this.teacherMapper.find(id.getValue())
            .map(this.teacherConverter::convertToDomain);
    }

    @Override
    public void save(@NonNull Teacher entity) {
        var data = this.teacherConverter.convertToData(entity);
        this.teacherMapper.save(data);
    }

    @Override
    public void remove(@NonNull Teacher entity) {
        var data = this.teacherConverter.convertToData(entity);
        this.teacherMapper.remove(data);
    }
}
