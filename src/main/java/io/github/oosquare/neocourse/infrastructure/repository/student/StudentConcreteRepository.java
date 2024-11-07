package io.github.oosquare.neocourse.infrastructure.repository.student;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.student.service.StudentRepository;
import io.github.oosquare.neocourse.utility.id.Id;

@Repository
@AllArgsConstructor
public class StudentConcreteRepository implements StudentRepository {

    private final @NonNull StudentMapper studentMapper;
    private final @NonNull StudentConverter studentConverter;

    @Override
    public Optional<Student> findByUsername(@NonNull Username username) {
        return this.studentMapper.findByUsername(username.getValue())
            .map(this.studentConverter::convertToDomain);
    }

    @Override
    public Optional<Student> find(@NonNull Id id) {
        return this.studentMapper.find(id.getValue())
            .map(this.studentConverter::convertToDomain);
    }

    @Override
    public void save(@NonNull Student entity) {
        var data = this.studentConverter.convertToData(entity);
        this.studentMapper.save(data);
    }

    @Override
    public void remove(@NonNull Student entity) {
        var data = this.studentConverter.convertToData(entity);
        this.studentMapper.remove(data);
    }
}
