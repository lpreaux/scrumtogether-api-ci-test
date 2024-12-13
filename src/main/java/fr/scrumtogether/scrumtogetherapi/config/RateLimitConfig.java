package fr.scrumtogether.scrumtogetherapi.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "rate-limit")
@Data
public class RateLimitConfig {
    private double requestsPerMinute = 2.0;
    private int expirationMinutes = 15;

    private final LoadingCache<String, RateLimiter> rateLimiters = CacheBuilder.newBuilder()
            .expireAfterAccess(expirationMinutes, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                @NonNull
                public RateLimiter load(@NonNull String key) {
                    // Allow 2 requests per minute
                    return RateLimiter.create(requestsPerMinute / 60.0);
                }
            });

    public RateLimiter getRateLimiter(String key) {
        return rateLimiters.getUnchecked(key);
    }
}
