package scrumtogether.scrumtogetherapi.security;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.google.common.cache.CacheBuilder;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LoginAttemptFilter extends OncePerRequestFilter {
    private final LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptFilter() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    @Nonnull
                    public Integer load(@Nonnull String key) {
                        return 0;
                    }
                });
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        String ipAddress = request.getRemoteAddr();

        if (isAuthenticationEndpoint(request) && isLoginAttempt(request)) {
            try {
                int attempts = attemptsCache.get(ipAddress);
                if (attempts >= 5) {
                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                    response.getWriter().write("Too many login attempts. Please try again later.");
                    return;
                }
                attemptsCache.put(ipAddress, attempts + 1);
            } catch (ExecutionException e) {
                log.error("Error accessing login attempts cache", e);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Unable to process request. Please try again later.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticationEndpoint(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/v1/sign-in");
    }

    private boolean isLoginAttempt(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }
}
