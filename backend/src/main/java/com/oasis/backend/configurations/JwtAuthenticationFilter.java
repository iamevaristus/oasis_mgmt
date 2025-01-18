package com.oasis.backend.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oasis.backend.configurations.exceptions.ApiResponseExceptionHandler;
import com.oasis.backend.configurations.exceptions.OasisException;
import com.oasis.backend.core.session.SessionService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The JwtAuthenticationFilter class is responsible for authenticating requests using JWT (JSON Web Token).
 * It extends OncePerRequestFilter, ensuring that it is only executed once per request.
 * <p></p>
 * This filter intercepts incoming requests, extracts the JWT token from the Authorization header,
 * validates the token, loads user details, and sets up the authentication context if the token is valid.
 * It also handles various exceptions related to JWT processing and authentication.
 *
 * @see OncePerRequestFilter
 * @see UserDetailsService
 * @see SessionService
 */
@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final SessionService sessionService;
    private final ApiResponseExceptionHandler handler;

    /**
     * Filters each incoming HTTP request and performs JWT authentication.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     */
    @Override
    @SneakyThrows
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        System.out.printf("New Request from ip address: %s for %s%n", request.getRemoteAddr(), request.getServletPath());

        try {
            // Extract JWT token from the Authorization header
            String header = request.getHeader("Authorization");

            // If the Authorization header is missing or does not start with "Bearer", proceed to the next filter
            if(header == null || !header.startsWith("Bearer") || header.length() < 7){
                filterChain.doFilter(request, response);
            } else {
                // Validate the session associated with the JWT token
                authenticateJwtRequests(request, header);
                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException | MalformedJwtException | OasisException | SignatureException
                 | UnsupportedJwtException | IllegalArgumentException | StringIndexOutOfBoundsException e
        ) {
            SecurityContextHolder.clearContext();
            new ObjectMapper().writeValue(response.getOutputStream(), handler.handle(e));
        }
    }

    private void authenticateJwtRequests(HttpServletRequest request, String header) {
        String jwt = header.substring(7);

        var session = sessionService.validateSession(jwt);
        if(session.getStatus().is2xxSuccessful()) {
            // If the user is not already authenticated, set up the authentication context
            if(SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(session.getData());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
    }
}