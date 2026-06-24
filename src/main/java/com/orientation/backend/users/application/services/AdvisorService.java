package com.orientation.backend.users.application.services;

import com.orientation.backend.shared.application.exceptions.core.BusinessViolation;
import com.orientation.backend.shared.application.exceptions.core.ErrorCode;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.application.commands.advisors.CreateAdvisorCommand;
import com.orientation.backend.users.application.exceptions.advisors.CreatedAdvisorException;
import com.orientation.backend.users.domain.model.entities.Advisor;
import com.orientation.backend.users.domain.model.query.AdvisorSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import com.orientation.backend.users.domain.model.valueobjects.Phone;
import com.orientation.backend.users.domain.repository.AdvisorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdvisorService {

    private final AdvisorRepository advisorRepository;

    @Transactional
    public Advisor createAdvisor(CreateAdvisorCommand command){
        List<BusinessViolation> violations = new ArrayList<>();

        Dni dni = Dni.of(command.dni());

        if(advisorRepository.existsByDni(dni)){
            violations.add(new BusinessViolation(
                    "dni",
                    "DNI already exists",
                    ErrorCode.USER_ALREADY_EXISTS
            ));
        }

        if(!violations.isEmpty()){
            throw new CreatedAdvisorException(violations);
        }

        Advisor advisor = Advisor.builder()
                .dni(dni)
                .fullName(FullName.of(command.name(), command.surname()))
                .email(Optional.ofNullable(command.email()).map(Email::of))
                .phone(Optional.ofNullable(command.phone()).map(Phone::of))
                .build();

        return advisorRepository.save(advisor);
    }


    public PageResult<Advisor> searchAdvisors(AdvisorSearchCriteria criteria, Pagination pagination){
        return advisorRepository.search(criteria, pagination);
    }
}
