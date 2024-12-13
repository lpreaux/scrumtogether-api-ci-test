package fr.scrumtogether.scrumtogetherapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import fr.scrumtogether.scrumtogetherapi.services.JwtService;

import java.io.IOException;

/**
 * A custom filter that intercepts incoming HTTP requests to validate JWT-based authentication.
 * This filter extends {@code OncePerRequestFilter}, ensuring it executes once per request.
 * <p>
 * Core responsibilities:
 * - Extract the JWT token from the Authorization header.
 * - Validate the token's authenticity and expiration.
 * - Retrieve user details associated with the token.
 * - Populate the security context with authenticated user details if the token is valid.
 * - Delegate the request to the next filter in the filter chain after processing.
 * <p>
 * In case of exceptions during the token validation process, the filter uses
 * {@code HandlerExceptionResolver} to handle the exceptions gracefully.
 */
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Processes the incoming HTTP request to filter and validate JWT-based authentication.
     * Validates the token, retrieves user details if necessary, and sets the security context
     * with an authentication token if the JWT token is valid.
     *
     * @param request the HTTP request being processed.
     * @param response the HTTP response being generated.
     * @param filterChain the chain of filters to which the processing should proceed after execution.
     * @throws ServletException if an error occurs during request processing.
     * @throws IOException if an I/O error occurs during request processing.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String username = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
