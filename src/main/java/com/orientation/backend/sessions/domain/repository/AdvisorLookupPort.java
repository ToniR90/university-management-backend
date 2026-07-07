package com.orientation.backend.sessions.domain.repository;

import java.util.Optional;
import java.util.UUID;

public interface AdvisorLookupPort {
    Optional<UUID> findIdByDni(String dni);
}