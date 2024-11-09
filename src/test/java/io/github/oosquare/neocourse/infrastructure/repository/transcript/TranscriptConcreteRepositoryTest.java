package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.transcript.model.Score;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.domain.transcript.model.TranscriptItem;
import io.github.oosquare.neocourse.domain.transcript.service.TranscriptRepository;
import io.github.oosquare.neocourse.utility.annotation.InfrastructureTestTag;
import io.github.oosquare.neocourse.utility.id.Id;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    TranscriptConcreteRepository.class,
    TranscriptMapper.class,
    TranscriptConverter.class,
    TranscriptItemConverter.class,
})
@InfrastructureTestTag
class TranscriptRepositoryTest {

    @Autowired
    private TranscriptRepository transcriptRepository;

    private Transcript testTranscript;

    @BeforeEach
    public void setUp() {
        this.testTranscript = Transcript.builder()
            .id(Id.of("transcript1"))
            .plan(Id.of("plan1"))
            .courseScores(new HashMap<>(Map.of(
                Id.of("course1"),
                TranscriptItem.ofInternally(
                    Id.of("course1"),
                    ClassPeriod.of(1),
                    Score.of(85),
                    true,
                    false
                )
            )))
            .build();

        this.transcriptRepository.save(this.testTranscript);
    }

    @AfterEach
    public void tearDown() {
        this.transcriptRepository.remove(this.testTranscript);
    }

    @Test
    public void findWhenDataExists() {
        var transcript = this.transcriptRepository.find(this.testTranscript.getId()).orElseThrow();
        assertEquals(this.testTranscript.getId(), transcript.getId());
        assertEquals(this.testTranscript.getPlan(), transcript.getPlan());
        assertEquals(
            this.testTranscript.getCourseScores().get(Id.of("course1")),
            transcript.getCourseScores().get(Id.of("course1"))
        );
    }

    @Test
    public void findWhenDataDoesNotExist() {
        var res = this.transcriptRepository.find(Id.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void save() {
        var newRegistration = TranscriptItem.ofInternally(
            Id.of("course1"),
            ClassPeriod.of(2),
            Score.of(92),
            true,
            false
        );
        var updatedTranscript = this.transcriptRepository.find(this.testTranscript.getId()).orElseThrow();
        updatedTranscript.getCourseScores().put(newRegistration.getCourse(), newRegistration);
        this.transcriptRepository.save(updatedTranscript);

        var transcript = this.transcriptRepository.find(updatedTranscript.getId()).orElseThrow();
        assertEquals(
            Optional.of(Score.of(92)),
            transcript.getCourseScores().get(Id.of("course1")).getScore()
        );
    }

    @Test
    public void remove() {
        this.transcriptRepository.remove(this.testTranscript);
        var res = this.transcriptRepository.find(this.testTranscript.getId());
        assertTrue(res.isEmpty());
    }
}
