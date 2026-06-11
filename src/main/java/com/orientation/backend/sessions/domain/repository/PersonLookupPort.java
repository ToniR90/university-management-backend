package com.orientation.backend.sessions.domain.repository;

import java.util.Optional;
import java.util.UUID;

public interface PersonLookupPort {
    Optional<UUID> findIdByDni(String dni);
}
