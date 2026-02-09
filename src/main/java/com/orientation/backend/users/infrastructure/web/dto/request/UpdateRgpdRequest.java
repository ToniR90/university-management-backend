package com.orientation.backend.users.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for updating RGPD consent status.
 * signedYear is only required for ALREADY_SIGNED status (validated in domain).
 */
public record UpdateRgpdRequest(

        @NotBlank(message = "L'estat de RGPD és obligatori")
        String rgpdConsentStatus,

        Integer signedYear
) {}