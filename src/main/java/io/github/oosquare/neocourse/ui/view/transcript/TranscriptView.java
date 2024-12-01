package io.github.oosquare.neocourse.ui.view.transcript;

import jakarta.annotation.security.PermitAll;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.query.transcript.TranscriptByAccountQuery;
import io.github.oosquare.neocourse.application.query.transcript.TranscriptDetailedRepresentation;
import io.github.oosquare.neocourse.application.query.transcript.TranscriptItemDetailedRepresentation;
import io.github.oosquare.neocourse.application.query.transcript.TranscriptQueryService;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "transcripts", layout = MainLayout.class)
@PageTitle("Transcript | NeoCourse")
@PermitAll
public class TranscriptView extends VerticalLayout
    implements CurrentAccountAwareSupport, HasUrlParameter<String> {

    public static final String CURRENT_ACCOUNT_TRANSCRIPT_PATH = "self";

    private final @NonNull TranscriptQueryService transcriptQueryService;

    private final @NonNull TextField studentNameField;
    private final @NonNull TextField scoreField;
    private final @NonNull Grid<TranscriptItemDetailedRepresentation> transcriptItemGrid;

    private String accountId;

    public TranscriptView(@NonNull TranscriptQueryService transcriptQueryService) {
        this.transcriptQueryService = transcriptQueryService;

        var title = new H3("Transcript");

        this.studentNameField = new TextField("Student Name");
        this.studentNameField.setReadOnly(true);

        this.scoreField = new TextField("Score");
        this.scoreField.setReadOnly(true);

        var summaryLayout = new HorizontalLayout(this.studentNameField, this.scoreField);
        summaryLayout.setPadding(false);

        this.transcriptItemGrid = createTranscriptItemGrid();

        this.add(title, summaryLayout, this.transcriptItemGrid);
    }

    private static Grid<TranscriptItemDetailedRepresentation> createTranscriptItemGrid() {
        var transcriptGrid = new Grid<>(TranscriptItemDetailedRepresentation.class, false);
        transcriptGrid.addColumn(TranscriptItemDetailedRepresentation::getCourseName)
            .setHeader("Course Name")
            .setAutoWidth(true)
            .setFlexGrow(3);
        transcriptGrid.addColumn(TranscriptItemDetailedRepresentation::getClassPeriod)
            .setHeader("Class Period")
            .setAutoWidth(true);
        transcriptGrid.addColumn(item -> item.getEvaluated() ? "Yes" : "No")
            .setHeader("Evaluated")
            .setAutoWidth(true);
        transcriptGrid.addColumn(TranscriptItemDetailedRepresentation::getParticipationStatus)
            .setHeader("Participation")
            .setAutoWidth(true);
        transcriptGrid.addColumn(item -> item.getEvaluated() ? item.getScore().toString() : "None")
            .setHeader("Score")
            .setAutoWidth(true);
        return transcriptGrid;
    }

    @Override
    public void setParameter(BeforeEvent event, String mayBeAccountId) {
        if (CURRENT_ACCOUNT_TRANSCRIPT_PATH.equals(mayBeAccountId)) {
            this.accountId = this.getCurrentAccount().getId().getValue();
        } else {
            this.accountId = mayBeAccountId;
        }
        this.updateView();
    }

    private void updateView() {
        var transcript = getTranscriptByAccount();

        this.studentNameField.setValue(transcript.getStudentName());
        this.scoreField.setValue(transcript.getScore().toString());
        this.transcriptItemGrid.setItems(transcript.getItems());
    }

    private TranscriptDetailedRepresentation getTranscriptByAccount() {
        var query = TranscriptByAccountQuery.builder()
            .accountId(Id.of(this.accountId))
            .build();
        var account = this.getCurrentAccount();
        return this.transcriptQueryService.getTranscriptByAccountInDetailedRepresentation(
            query,
            account
        );
    }
}
