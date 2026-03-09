package com.see.visal.admin_e_backing.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {



    @Bean
    public SecurityWebFilterChain webSecurity(ServerHttpSecurity http) {

        http.authorizeExchange(exchange -> exchange
//                        .pathMatchers("/account/public/**").permitAll()
                       .anyExchange().authenticated()
                     //   .anyExchange().permitAll()
                );
        // Disable default security mechanisms
           http.csrf(csrfSpec -> csrfSpec.disable());
           http.formLogin(formLoginSpec -> formLoginSpec.disable());
         //  http.logout(logoutSpec -> logoutSpec.disable());
           http.httpBasic(httpBasicSpec -> httpBasicSpec.disable());

          http.oauth2Login(Customizer.withDefaults());
        //       http.oauth2ResourceServer(oauth2 -> oauth2
        //      .jwt(Customizer.withDefaults()));


        return http.build();
    }

}
