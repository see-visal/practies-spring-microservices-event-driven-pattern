package com.see.visal.front_bff.dto;

import lombok.Builder;



@Builder
public record ProfileResponse(
        String username,
        String fullName,
        String email,
        String familyName,
        String givenName,
        String phoneNumber,
        String gender,
        String birthdate,
        String picture,
        String coverImage,
        java.util.Set<String> roles,
        java.util.Set<String> permission

) {
}
