package com.orientation.backend.sessions.application.services;

import com.orientation.backend.sessions.application.commands.AddAssistantCommand;
import com.orientation.backend.sessions.application.commands.CancelSessionCommand;
import com.orientation.backend.sessions.application.commands.CreateSessionCommand;
import com.orientation.backend.sessions.application.commands.RemoveAssistantCommand;
import com.orientation.backend.sessions.domain.model.entities.Assistant;
import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.enums.SessionOrigin;
import com.orientation.backend.sessions.domain.model.enums.SessionType;
import com.orientation.backend.sessions.domain.model.exceptions.AssistantNotFoundException;
import com.orientation.backend.sessions.domain.model.exceptions.PersonNotFoundException;
import com.orientation.backend.sessions.domain.model.exceptions.SessionNotFoundException;
import com.orientation.backend.sessions.domain.model.query.SessionSearchCriteria;
import com.orientation.backend.sessions.domain.repository.AssistantRepository;
import com.orientation.backend.sessions.domain.repository.PersonLookupPort;
import com.orientation.backend.sessions.domain.repository.SessionRepository;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final PersonLookupPort personLookupPort;
    private final AssistantRepository assistantRepository;

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

    @Transactional
    public void addAssistant(AddAssistantCommand command) {
        if(sessionRepository.findById(command.sessionId()).isEmpty()) {
            throw new SessionNotFoundException();
        }

        UUID personId = personLookupPort.findIdByDni(command.personDni())
                .orElseThrow(PersonNotFoundException::new);

        Assistant assistant = Assistant.builder()
                .sessionId(command.sessionId())
                .personId(personId)
                .registered(true)
                .attended(false)
                .build();

        assistantRepository.add(assistant);
    }

    @Transactional
    public void removeAssistant(RemoveAssistantCommand command) {

        UUID personId = personLookupPort.findIdByDni(command.personDni())
                .orElseThrow(PersonNotFoundException::new);

        boolean exists = assistantRepository.findBySessionId(command.sessionId())
                .stream()
                .anyMatch(a -> a.getPersonId().equals(personId));

        if(!exists) {
            throw new AssistantNotFoundException();
        }

        assistantRepository.remove(command.sessionId(), personId);
    }
}
