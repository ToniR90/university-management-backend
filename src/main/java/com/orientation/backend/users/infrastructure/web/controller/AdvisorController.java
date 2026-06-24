package com.orientation.backend.users.infrastructure.web.controller;

import com.orientation.backend.users.application.commands.advisors.CreateAdvisorCommand;
import com.orientation.backend.users.application.services.AdvisorService;
import com.orientation.backend.users.infrastructure.web.dto.response.AdvisorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/advisors")
@RequiredArgsConstructor
public class AdvisorController {

    private final AdvisorService advisorService;

    @PostMapping
    public ResponseEntity<AdvisorResponse> createAdvisor(@Valid @RequestBody  command)
}
