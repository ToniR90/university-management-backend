package com.orientation.backend.sessions.infraestructure.persistance.adapters;

import com.orientation.backend.sessions.domain.repository.AdvisorLookupPort;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.repository.AdvisorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdvisorLookupAdapter implements AdvisorLookupPort {

    private final AdvisorRepository advisorRepository;

    @Override
    public Optional<UUID> findIdByDni(String dni) {
        return advisorRepository.findAdvisorIdByDni(Dni.of(dni));
    }
}