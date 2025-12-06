package org.example.app_e_learning.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@EnableMethodSecurity
public class CoursesController {

    // Liste statique de cours pour tests
    private final List<Map<String, Object>> courses = new ArrayList<>();

    public CoursesController() {
        courses.add(Map.of("id", 1, "title", "Introduction au Réseau", "author", "Prof A"));
        courses.add(Map.of("id", 2, "title", "Sécurité des Systèmes", "author", "Prof B"));
    }

    // GET /courses - accessible STUDENT et ADMIN
    @GetMapping("/courses")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public List<Map<String, Object>> getCourses() {
        return courses;
    }

    // POST /courses - accessible ADMIN uniquement
    @PostMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> addCourse(@RequestBody Map<String, Object> payload, Authentication authentication) {
        int newId = courses.size() + 1;
        Map<String, Object> course = new HashMap<>(payload);
        course.put("id", newId);
        course.put("createdBy", authentication.getName());
        courses.add(course);
        return course;
    }

    // GET /me - renvoie les infos utilisateur et rôles depuis Keycloak
    @GetMapping("/me")
    public Map<String, Object> getMe(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> result = new HashMap<>();
        result.put("username", jwt.getClaimAsString("preferred_username"));

        // Récupérer les rôles depuis "realm_access.roles"
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = new ArrayList<>();
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            roles = (List<String>) realmAccess.get("roles");
        }
        result.put("roles", roles);

        return result;
    }

    // Endpoint public pour test
    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello World (public)";
    }
}
