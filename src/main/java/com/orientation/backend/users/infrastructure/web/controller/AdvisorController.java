package com.orientation.backend.users.infrastructure.web.controller;

import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.application.commands.advisors.CreateAdvisorCommand;
import com.orientation.backend.users.application.services.AdvisorService;
import com.orientation.backend.users.domain.model.entities.Advisor;
import com.orientation.backend.users.domain.model.query.AdvisorSearchCriteria;
import com.orientation.backend.users.infrastructure.web.dto.request.CreateAdvisorRequest;
import com.orientation.backend.users.infrastructure.web.dto.response.AdvisorResponse;
import com.orientation.backend.users.infrastructure.web.dto.response.PagedAdvisorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/advisors")
@RequiredArgsConstructor
public class AdvisorController {

    private final AdvisorService advisorService;

    @PostMapping
    public ResponseEntity<AdvisorResponse> createAdvisor(@Valid @RequestBody CreateAdvisorRequest request){
        CreateAdvisorCommand command = new CreateAdvisorCommand(
                request.dni(),
                request.name(),
                request.surname(),
                request.email(),
                request.phone()
        );

        Advisor advisor = advisorService.createAdvisor(command);
        AdvisorResponse response = AdvisorResponse.fromDomain(advisor);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PagedAdvisorResponse> getAllAdvisors(@RequestParam (defaultValue = "0") int page,
                                                               @RequestParam (defaultValue = "20") int size,
                                                               @RequestParam (required = false) String name,
                                                               @RequestParam (required = false) String dni){

        Pagination pagination = new Pagination(page, size);
        AdvisorSearchCriteria criteria = new AdvisorSearchCriteria(name, dni);
        PageResult<Advisor> advisors = advisorService.searchAdvisors(criteria, pagination);

        return ResponseEntity.ok(PagedAdvisorResponse.fromDomain(advisors));
    }
}
