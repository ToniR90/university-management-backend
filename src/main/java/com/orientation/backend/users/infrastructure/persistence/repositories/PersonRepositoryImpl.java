package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.repository.PersonRepository;
import com.orientation.backend.users.infrastructure.persistence.entities.PersonJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PersonRepositoryImpl implements PersonRepository {

    private final SpringDataPersonRepository jpaRepository;

    @Override
    public Optional<UUID> findIdByDni(Dni dni) {
        return jpaRepository.findByDniAndActiveTrue(dni.getValue()).map(PersonJpaEntity::getId);
    }
}
