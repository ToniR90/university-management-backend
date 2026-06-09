package com.orientation.backend.sessions.application.services;

import com.orientation.backend.sessions.application.commands.CancelSessionCommand;
import com.orientation.backend.sessions.application.commands.CreateSessionCommand;
import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.enums.SessionOrigin;
import com.orientation.backend.sessions.domain.model.enums.SessionType;
import com.orientation.backend.sessions.domain.model.exceptions.SessionNotFoundException;
import com.orientation.backend.sessions.domain.model.query.SessionSearchCriteria;
import com.orientation.backend.sessions.domain.repository.SessionRepository;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .startDateTime(command.startDateTime())
                .build();

        return sessionRepository.save(session);
    }

    public List<Session> findCancellable(String title){
        SessionSearchCriteria criteria = new SessionSearchCriteria(title);
        return sessionRepository.findCancellable(criteria);
    }

    @Transactional
    public void cancelSession(CancelSessionCommand command){
        Session session = sessionRepository.findById(command.id())
                .orElseThrow(() -> new SessionNotFoundException());

        session.cancel(command.cancelReason());
        sessionRepository.save(session);
    }

    public PageResult<Session> searchSessions(SessionSearchCriteria criteria, Pagination pagination) {
        return sessionRepository.search(criteria, pagination);
    }
}
