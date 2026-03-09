package com.see.visal.itp_indentity.security;


import com.see.visal.itp_indentity.domain.Role;
import com.see.visal.itp_indentity.domain.User;
import com.see.visal.itp_indentity.features.oauth2.JpaRegisteredClientRepository;
import com.see.visal.itp_indentity.features.role.RoleRepository;
import com.see.visal.itp_indentity.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityInit {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUuid(UUID.randomUUID().toString());
            user.setUsername("visal");
            user.setPassword(passwordEncoder.encode("123"));
            user.setEmail("visalsoeurn9@gmail.com");
            user.setDob(LocalDate.of(2003, 6, 6));
            user.setGender("Male");
            user.setProfileImage("default_profile.jpg");
            user.setCoverImage("default_cover.jpg");
            user.setFamilyName("Soruen");
            user.setGivenName("Visal");
            user.setPhoneNumber("0963001940");
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setIsEnabled(true);

            // Assign role to user
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("SUPER_ADMIN"));
            roles.add(roleRepository.findByName("USER"));
            user.setRoles(roles);

            userRepository.save(user);
            log.info("User has been saved: {}", user.getId());
        }
    }


    @PostConstruct
    void initOAuth2() {

        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofDays(3))
                .reuseRefreshTokens(false) // refresh token rotation
                .refreshTokenTimeToLive(Duration.ofDays(5))
                .build();

        ClientSettings clientSettings = ClientSettings.builder()
                .requireProofKey(false)
                .requireAuthorizationConsent(false)
                .build();

        var itpStandard = RegisteredClient.withId("itp-standard")
                .clientId("itp-standard")
                .clientSecret(passwordEncoder.encode("qwerqwer")) // store in secret manager
                .scopes(scopes -> {
                    scopes.add(OidcScopes.OPENID); // required!
                    scopes.add(OidcScopes.PROFILE);
                    scopes.add(OidcScopes.EMAIL);
                })
                .redirectUris(uris -> {
                    // default redirect uri with port 9090 before put security into gateway
                    uris.add("http://localhost:9090/login/oauth2/code/itp-standard");
                    uris.add("http://localhost:9090");
                    uris.add("https://cstad.edu.kh/");

                    // after put security into gateway, need to add redirect uri with port 9999
                    uris.add("http://localhost:9999/login/oauth2/code/itp-standard");
                    uris.add("http://localhost:9999");

                })
                .postLogoutRedirectUris(uris -> {
                    uris.add("http://localhost:9090");
                })
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)//TODO: grant_type:client_credentials, client_id & client_secret, redirect_uri
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                    grantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();




        var itpFrontBff = RegisteredClient.withId("itp-front-bff")
                .clientId("itp-front-bff")
                .clientSecret(passwordEncoder.encode("qwerqwer")) // store in secret manager
                .scopes(scopes -> {
                    scopes.add(OidcScopes.OPENID); // required!
                    scopes.add(OidcScopes.PROFILE);
                    scopes.add(OidcScopes.EMAIL);
                })
                .redirectUris(uris -> {
                    // default redirect uri with port 9090 before put security into gateway
                    uris.add("http://localhost:9090/login/oauth2/code/itp-front-bff");
                    uris.add("http://localhost:9090");
                    uris.add("https://cstad.edu.kh/");

                    // after put security into gateway, need to add redirect uri with port 9999
                    uris.add("http://localhost:10000/login/oauth2/code/itp-front-bff");
                    uris.add("http://localhost:10000");

                })
                .postLogoutRedirectUris(uris -> {
                    uris.add("http://localhost:10000");
                })
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) //TODO: grant_type:client_credentials, client_id & client_secret, redirect_uri
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                    grantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();

        var adminEBanking = RegisteredClient.withId("admin-client")
                .clientId("admin-client")
                .clientSecret(passwordEncoder.encode("qwerqwer")) // store in secret manager
                .scopes(scopes -> {
                    scopes.add(OidcScopes.OPENID); // required!
                    scopes.add(OidcScopes.PROFILE);
                    scopes.add(OidcScopes.EMAIL);
                })
                .redirectUris(uris -> {
                    // default redirect uri with port 9090 before put security into gateway
                    uris.add("http://localhost:9090/login/oauth2/code/admin-client");
                    uris.add("http://localhost:9090");
                    uris.add("https://cstad.edu.kh/");

                    // after put security into gateway, need to add redirect uri with port 9999
                    uris.add("http://localhost:20000/login/oauth2/code/admin-client");
                    uris.add("http://localhost:20000");

                })
                .postLogoutRedirectUris(uris -> {
                    uris.add("http://localhost:9090");
                })
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)//TODO: grant_type:client_credentials, client_id & client_secret, redirect_uri
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                    grantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();



        RegisteredClient registeredClient = jpaRegisteredClientRepository.findByClientId("itp-standard");
        log.info("Registered client: {}", registeredClient);
        RegisteredClient registeredClient2 = jpaRegisteredClientRepository.findByClientId("itp-front-bff");
        log.info("Registered client: {}", registeredClient);

        RegisteredClient registeredClient3 = jpaRegisteredClientRepository.findByClientId("admin-e-banking");
        log.info("Registered client: {}", registeredClient);

        if (registeredClient == null) {
            jpaRegisteredClientRepository.save(itpStandard);
        }
        if (registeredClient2 == null) {
            jpaRegisteredClientRepository.save(itpFrontBff);
        }
        if (registeredClient3 == null) {
            jpaRegisteredClientRepository.save(adminEBanking);
        }
    }

}


