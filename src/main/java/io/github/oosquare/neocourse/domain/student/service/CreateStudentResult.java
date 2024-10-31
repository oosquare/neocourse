package io.github.oosquare.neocourse.domain.student.service;

import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;

@Value
public class CreateStudentResult {

    private final @NonNull Student student;
    private final @NonNull Transcript transcript;
}
