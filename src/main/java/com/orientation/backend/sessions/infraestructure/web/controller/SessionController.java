package com.orientation.backend.sessions.infraestructure.web.controller;

import com.orientation.backend.sessions.application.commands.CreateSessionCommand;
import com.orientation.backend.sessions.application.services.SessionService;
import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.infraestructure.web.dto.request.CreateSessionRequest;
import com.orientation.backend.sessions.infraestructure.web.dto.response.SessionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                request.allWelcome()
        );
        Session session = sessionService.createSession(command);
        SessionResponse response = SessionResponse.fromDomain(session);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
