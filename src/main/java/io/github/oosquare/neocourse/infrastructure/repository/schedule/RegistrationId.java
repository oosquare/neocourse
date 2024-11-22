package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationId implements Serializable {

    private String scheduleId;
    private String studentId;
}
