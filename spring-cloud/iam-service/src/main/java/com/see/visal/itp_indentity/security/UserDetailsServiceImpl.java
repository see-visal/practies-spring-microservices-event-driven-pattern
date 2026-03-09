package com.see.visal.itp_indentity.security;

import com.see.visal.itp_indentity.domain.Permission;
import com.see.visal.itp_indentity.domain.Role;
import com.see.visal.itp_indentity.domain.User;
import com.see.visal.itp_indentity.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username
//            ) throws UsernameNotFoundException {
//
//        // Load or find user from database
//        User loggedInUser = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException(username));
//
//        // Build UserDetails object
//        String[] roles = loggedInUser.getRoles().stream()
//                .map(Role::getName)
//                .toArray(String[]::new);
//
//        // Important for user authorities
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        loggedInUser.getRoles().forEach(role -> {
//            // Add role into authority
//            // authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
//            role.getPermissions().forEach(permission -> {
//                // Add permission into authority
//                authorities.add(new SimpleGrantedAuthority(permission.getName()));
//            });
//        });
//
//        UserDetails userSecurity = org.springframework.security.core.userdetails.User.builder()
//                .username(loggedInUser.getUsername())
//                .password(loggedInUser.getPassword())
//                //.roles(roles)
//                .authorities(authorities)
//                .build();
//        log.info("UserDetailsServiceImpl loadUserByUsername = {}", userSecurity.getAuthorities());
//        log.info("UserDetailsServiceImpl loadUserByUsername = {}", userSecurity.getUsername());
//
//        return userSecurity;
//    }
//}

public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        log.info("USER AUTHORITIES: {}", authorities);

//        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
//                .disabled(!user.getIsEnabled())
//                .credentialsExpired(!user.getCredentialsNonExpired())
//                .accountLocked(!user.getAccountNonLocked())
//                .accountExpired(!user.getAccountNonExpired())
////                .roles(user.getRoles().stream().map(Role::getName).toArray(String[]::new))
//                .authorities(authorities)
//                .password(user.getPassword())
//                .build();

        return new CustomUserDetails(
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFamilyName(),
                user.getGivenName(),
                user.getPhoneNumber(),
                user.getGender(),
                user.getDob(),
                user.getProfileImage(),
                user.getCoverImage(),
                user.getAccountNonExpired(),
                user.getAccountNonLocked(),
                user.getCredentialsNonExpired(),
                user.getIsEnabled(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                user.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet())
        );
    }
}

