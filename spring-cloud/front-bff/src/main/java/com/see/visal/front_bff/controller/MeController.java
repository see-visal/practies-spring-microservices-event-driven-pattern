//package com.see.visal.front_bff.controller;
//
//
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//public class MeController {
//    @GetMapping("/api/me")
//    public Map<String, Object> me(@AuthenticationPrincipal OidcUser user) {
//
//        if (user == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("authenticated", true);
//        result.put("username", user.getPreferredUsername());
//        result.put("email", user.getEmail());
//
//        return result;
//    }
//
//}
