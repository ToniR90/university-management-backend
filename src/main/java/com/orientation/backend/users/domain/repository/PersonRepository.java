package com.orientation.backend.users.domain.repository;

import com.orientation.backend.users.domain.model.valueobjects.Dni;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {
    Optional<UUID> findIdByDni(Dni dni);
}
