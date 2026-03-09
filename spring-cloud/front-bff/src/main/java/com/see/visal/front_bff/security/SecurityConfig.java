package com.see.visal.front_bff.security;//package com.see.visal.front_bff.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain webSecurity(ServerHttpSecurity http,
                                              ReactiveClientRegistrationRepository clientRegistrationRepository) {

        http.authorizeExchange(exchange -> exchange
                .pathMatchers("/profile/**", "/auth/**").authenticated()
                .anyExchange().permitAll()
        );

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        //http.logout(ServerHttpSecurity.LogoutSpec::disable);
        http.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);

        http.oauth2Login(Customizer.withDefaults());

        // customize logout
        http.logout(logoutSpec -> logoutSpec
                .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
        );

        return http.build();
    }


    // Custom  OIDC logout   for lout everything
    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");

        return oidcLogoutSuccessHandler;
    }

    // Client logout for logout,but some credential still exist
    private ServerLogoutSuccessHandler serverLogoutSuccessHandler() {

        RedirectServerLogoutSuccessHandler redirectServerLogoutSuccessHandler =
                new RedirectServerLogoutSuccessHandler();

        final String DEFAULT_LOGOUT_SUCCESS_URL = "/";

        URI logoutSuccessUrl = URI.create(DEFAULT_LOGOUT_SUCCESS_URL);

        redirectServerLogoutSuccessHandler.setLogoutSuccessUrl(logoutSuccessUrl);

        return redirectServerLogoutSuccessHandler;
    }


}

//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
//import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
//import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
//
//import java.net.URI;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
//
//        RedirectServerLogoutSuccessHandler logoutSuccessHandler =
//                new RedirectServerLogoutSuccessHandler();
//        logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/"));
//
//        return http
//                .authorizeExchange(exchange -> exchange
//                        .pathMatchers("/auth/**").authenticated()
//                        .pathMatchers("/profile").authenticated()
//                        .anyExchange().permitAll()
//                )
//                .logout(logoutSpec -> logoutSpec.logoutSuccessHandler(oidcLogoutSuccessHandler(reactiveClientRegistrationRepository)))
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
//                .oauth2Login(Customizer.withDefaults())
//                .build();
//    }
//
//    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
//        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler =
//                new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
//        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
//
//        return oidcLogoutSuccessHandler;
//    }
//
//}