
package com.see.visal.itp_indentity.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;



//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final PasswordEncoder passwordEncoder;
//    private final UserDetailsService userDetailService;
//
//    @Value("${spring.security.oauth2.authorizationserver.issuer}")
//    private String issuerUri;
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationConfigurer() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider;
//    }
//
//    @Bean
//    AuthorizationServerSettings authorizationServerSettings() {
//        return AuthorizationServerSettings.builder()
//                .issuer(issuerUri)
//                .build();
//    }
//
//    @Bean
//    @Order(1)
//    public SecurityFilterChain configureOAuth2(HttpSecurity http) throws Exception {
//        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
//                new OAuth2AuthorizationServerConfigurer();
//
//        http
//                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
//                .with(authorizationServerConfigurer, (authorizationServer) ->
//                        authorizationServer.oidc(Customizer.withDefaults())
//                )
//                .authorizeHttpRequests((authorize) ->
//                        authorize.anyRequest().authenticated()
//                )
//                .exceptionHandling(ex -> ex
//                        .defaultAuthenticationEntryPointFor(
//                                new LoginUrlAuthenticationEntryPoint("/login"),
//                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
//                        )
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    @Order(2)
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(request -> request
//                        // Allow static resources and login/logout pages
//                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/login")
//                        .defaultSuccessUrl("http://localhost:10000", true)
//                        .permitAll()
//                )
//                // --- LOGOUT CONFIGURATION ---
//                  .logout(logout -> logout
//                                .logoutRequestMatcher(request ->
//                                        request.getServletPath().equals("/logout")
//                                               &&
//                                                request.getMethod().equals("POST")
//                                )
//                                 .logoutUrl("/logout") // The URL to trigger logout
//
//
//                                  .logoutSuccessUrl("http://localhost:10000")
//                                  .logoutSuccessUrl("/login?logout")
//                                  .invalidateHttpSession(true)
//                                  .deleteCookies("JSESSIONID")
//                                  .permitAll()
//                  )
//
//                // -----------------------------
//                .cors(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }
//}

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder encoder;
    private final UserDetailsService userDetailsService;

    @Value("${spring.security.oauth2.authorizationserver.issuer}")
    private String issuerUri;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(encoder);

        return provider;
    }


    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings
                .builder()
                .issuer(issuerUri)
                .build();
    }


    @Bean
    @Order(1)
    public SecurityFilterChain configureOAuth2(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        //http.apply(authorizationServerConfigurer);
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .oidc(Customizer.withDefaults())	// Initialize `OidcConfigurer`
                )
                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated()
                );

        http
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );


        return http.build();
    }




    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        // Allow static resources and login/logout pages
                        .requestMatchers("/public/**","/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated())

//                .formLogin(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                       // .defaultSuccessUrl("http://localhost:10000", true)
                     //   .defaultSuccessUrl("http://localhost:20000", true)
                        .permitAll()
               )
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
// for adding more data of user into jwt token
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                Authentication principal = context.getPrincipal();

                if (principal.getPrincipal() instanceof CustomUserDetails userDetails) {
                    context.getClaims().claims(claims -> {
                        claims.put("email", userDetails.getEmail());
                        claims.put("family_name", userDetails.getFamilyName());
                        claims.put("given_name", userDetails.getGivenName());
                        claims.put("phone_number", userDetails.getPhoneNumber());
                        claims.put("gender", userDetails.getGender());
                        claims.put("birthdate", userDetails.getDob().toString());
                        claims.put("picture", userDetails.getProfileImage());
                        claims.put("cover_image", userDetails.getCoverImage());
                        claims.put("roles", userDetails.getRoles());
                        claims.put("permissions", userDetails.getPermissions());
                    });
                }
            }
        };
    }


}

