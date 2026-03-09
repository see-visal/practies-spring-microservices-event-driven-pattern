package com.see.visal.front_bff.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        Boolean isAuthenticated,
        String name
) {
}
