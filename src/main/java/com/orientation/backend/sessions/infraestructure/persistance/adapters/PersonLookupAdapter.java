package com.orientation.backend.sessions.infraestructure.persistance.adapters;

import com.orientation.backend.sessions.domain.repository.PersonLookupPort;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PersonLookupAdapter implements PersonLookupPort {

    private final PersonRepository personRepository;

    @Override
    public Optional<UUID> findIdByDni(String dni) {
        return personRepository.findIdByDni(Dni.of(dni));
    }
}