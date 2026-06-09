package com.orientation.backend.sessions.infraestructure.web.controller;

import com.orientation.backend.sessions.application.commands.CancelSessionCommand;
import com.orientation.backend.sessions.application.commands.CreateSessionCommand;
import com.orientation.backend.sessions.application.services.SessionService;
import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.query.SessionSearchCriteria;
import com.orientation.backend.sessions.infraestructure.web.dto.request.CancelSessionRequest;
import com.orientation.backend.sessions.infraestructure.web.dto.request.CreateSessionRequest;
import com.orientation.backend.sessions.infraestructure.web.dto.response.CancellableSessionResponse;
import com.orientation.backend.sessions.infraestructure.web.dto.response.PagedSessionResponse;
import com.orientation.backend.sessions.infraestructure.web.dto.response.SessionResponse;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponse> createSession(@Valid @RequestBody CreateSessionRequest request){
        CreateSessionCommand command = new CreateSessionCommand(
                request.title(),
                request.description(),
                request.motivation(),
                request.sessionType(),
                request.sessionOrigin(),
                request.allWelcome(),
                request.startDateTime()
        );
        Session session = sessionService.createSession(command);
        SessionResponse response = SessionResponse.fromDomain(session);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PagedSessionResponse> getAllSessions(@RequestParam (defaultValue = "0") int page,
                                                               @RequestParam (defaultValue = "20") int size,
                                                               @RequestParam (required = false) String title) {
        Pagination pagination = new Pagination(page, size);
        SessionSearchCriteria criteria = new SessionSearchCriteria(title);
        PageResult<Session> sessions = sessionService.searchSessions(criteria, pagination);

        return ResponseEntity.ok(PagedSessionResponse.fromDomain(sessions));
    }

    @GetMapping("/cancellable")
    public ResponseEntity<List<CancellableSessionResponse>> findCancellable(
            @RequestParam(required = false) String title) {

        List<Session> session = sessionService.findCancellable(title);

        List<CancellableSessionResponse> response = session.stream()
                .map(CancellableSessionResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSession(@PathVariable UUID id, @RequestBody CancelSessionRequest request){

        CancelSessionCommand command = new CancelSessionCommand(id, request.cancelReason());
        sessionService.cancelSession(command);

        return ResponseEntity.noContent().build();
    }
}
