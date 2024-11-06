package io.github.oosquare.neocourse.infrastructure.repository.admin;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorRepository;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.utility.id.Id;

@Repository
@AllArgsConstructor
public class AdministratorConcreteRepository implements AdministratorRepository {

    private final @NonNull AdministratorMapper administratorMapper;
    private final @NonNull AdministratorConverter administratorConverter;

    @Override
    public Optional<Administrator> findByUsername(@NonNull Username username) {
        return this.administratorMapper.findByUsername(username.getValue())
            .map(this.administratorConverter::convertToDomain);
    }

    @Override
    public Optional<Administrator> find(@NonNull Id id) {
        return this.administratorMapper.find(id.getValue())
            .map(this.administratorConverter::convertToDomain);
    }

    @Override
    public void save(@NonNull Administrator entity) {
        var data = this.administratorConverter.convertToData(entity);
        this.administratorMapper.save(data);
    }

    @Override
    public void remove(@NonNull Administrator entity) {
        var data = this.administratorConverter.convertToData(entity);
        this.administratorMapper.remove(data);
    }
}
