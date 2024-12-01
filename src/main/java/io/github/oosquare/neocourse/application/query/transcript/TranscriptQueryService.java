package io.github.oosquare.neocourse.application.query.transcript;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.account.service.AccountService;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.transcript.service.TranscriptRepository;
import io.github.oosquare.neocourse.infrastructure.repository.transcript.TranscriptMapper;

@Service
@AllArgsConstructor
@Slf4j
public class TranscriptQueryService {

    private final @NonNull AccountRepository accountRepository;
    private final @NonNull PlanRepository planRepository;
    private final @NonNull TranscriptRepository transcriptRepository;
    private final @NonNull AccountService accountService;
    private final @NonNull TranscriptMapper transcriptMapper;

    @Transactional
    public TranscriptDetailedRepresentation getTranscriptByAccountInDetailedRepresentation(
        @NonNull TranscriptByAccountQuery query,
        @NonNull Account account
    ) {
        log.info(
            "{} requests getTranscriptByAccountInDetailedRepresentation with {}",
            account.toLoggingString(),
            query
        );

        var accountId = query.getAccountId();

        var accountToQuery = this.accountRepository.findOrThrow(accountId);
        var student = this.accountService.getStudentUser(accountToQuery);
        var transcript = this.transcriptRepository.findOrThrow(student.getTranscript());
        var plan = this.planRepository.findOrThrow(transcript.getPlan());
        var itemData = this.transcriptMapper.findAllByTranscriptReturningDetailedProjection(
            transcript.getId().getValue()
        );

        return TranscriptDetailedRepresentation.fromDomainAndData(transcript, student, plan, itemData);
    }
}
