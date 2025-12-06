package org.example.app_e_learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // endpoints publics
                        .anyRequest().authenticated()             // tout le reste sécurisé
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractKeycloakRoles);
        return converter;
    }

    private Collection<GrantedAuthority> extractKeycloakRoles(Jwt jwt) {
        // 1) roles dans realm_access.roles
        Object realmAccess = jwt.getClaim("realm_access");
        if (realmAccess instanceof Map) {
            Map<?, ?> realmMap = (Map<?, ?>) realmAccess;
            Object rolesObj = realmMap.get("roles");
            if (rolesObj instanceof List) {
                List<?> roles = (List<?>) rolesObj;
                return roles.stream()
                        .map(Object::toString)
                        // ne pas ajouter "ROLE_" car tes rôles Keycloak sont déjà ROLE_STUDENT / ROLE_ADMIN
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
        }

        // 2) fallback: roles dans resource_access.<client>.roles
        Object resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess instanceof Map) {
            Map<?, ?> resourceMap = (Map<?, ?>) resourceAccess;
            return resourceMap.values().stream()
                    .filter(v -> v instanceof Map)
                    .map(v -> (Map<?, ?>) v)
                    .map(m -> m.get("roles"))
                    .filter(r -> r instanceof List)
                    .flatMap(r -> ((List<?>) r).stream())
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return List.of(); // aucun rôle trouvé
    }
}
