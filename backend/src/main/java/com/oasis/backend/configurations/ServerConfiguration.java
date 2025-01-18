package com.oasis.backend.configurations;

import com.oasis.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ServerConfiguration {
    private final UserRepository userRepository;

    /**
     * Configures an AuthenticationManager bean for managing authentication.
     *
     * @param authenticationConfiguration The authentication configuration.
     * @return An AuthenticationManager instance.
     * @throws Exception If an error occurs while retrieving the authentication manager.
     */
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures a UserDetailsService bean for retrieving user details during authentication.
     *
     * @return A UserDetailsService instance.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmailAddressIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Configures an AuthenticationProvider bean for authentication.
     *
     * @return An AuthenticationProvider instance.
     */
    @Bean
    public AuthenticationProvider authProvider() {
        var daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService());
        daoProvider.setPasswordEncoder(passwordEncoder());

        return daoProvider;
    }

    /**
     * Configures a PasswordEncoder bean for encoding passwords.
     *
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures a CorsConfigurationSource bean for configuring CORS settings.
     *
     * @return A CorsConfigurationSource instance.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", getCorsConfiguration());

        return source;
    }

    private CorsConfiguration getCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("/**"));
        configuration.setAllowedOriginPatterns(getAllowedOriginPatterns());
        configuration.setAllowedMethods(getAllowedMethods());
        configuration.setAllowedHeaders(getAllowedHeaders());
        configuration.setAllowCredentials(true);

        return configuration;
    }

    private List<String> getAllowedOriginPatterns() {
        return List.of("http://localhost:*", "http://127.0.0.1:*", "/**");
    }

    private List<String> getAllowedMethods() {
        return List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name()
        );
    }

    private List<String> getAllowedHeaders() {
        return List.of(
                "X-CSRF-TOKEN",
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.ACCEPT,
                HttpHeaders.CONTENT_TYPE
        );
    }
}