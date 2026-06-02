package com.orientation.backend.sessions.application.services;

import com.orientation.backend.sessions.application.commands.CreateSessionCommand;
import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.enums.SessionOrigin;
import com.orientation.backend.sessions.domain.model.enums.SessionType;
import com.orientation.backend.sessions.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public Session createSession(CreateSessionCommand command){

        Session session = Session.builder()
                .title(command.title())
                .description(command.description())
                .motivation(command.motivation())
                .sessionType(SessionType.fromString(command.sessionType()))
                .sessionOrigin(SessionOrigin.fromString(command.sessionOrigin()))
                .allWelcome(command.allWelcome())
                .build();

        return sessionRepository.save(session);
    }
}
