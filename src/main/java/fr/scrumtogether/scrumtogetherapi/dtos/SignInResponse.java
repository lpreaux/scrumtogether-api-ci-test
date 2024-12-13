package fr.scrumtogether.scrumtogetherapi.dtos;

import fr.scrumtogether.scrumtogetherapi.entities.User;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link User}
 * <p>
 * Represents the response returned to the client upon successful user sign-in.
 * This class encapsulates the authentication token and its expiration details.
 * The class is immutable and implements {@link Serializable} for distributed system compatibility.
 */
@Value
public class SignInResponse implements Serializable {

    /**
     * The JWT authentication token issued upon successful sign-in.
     * Used for subsequent request authentication.
     * <p>
     * Format: Bearer token that should be included in the Authorization header
     * of subsequent HTTP requests.
     */
    String token;

    /**
     * The token's validity duration in seconds from the time of issuance.
     * Indicates when a new token should be obtained through re-authentication.
     * <p>
     * Note: This value should be used by clients to schedule token refresh
     * before the current token expires.
     */
    Long expiresIn;

    /**
     * Creates a new SignInResponse with the provided token and expiration time.
     *
     * @param token     the JWT authentication token
     * @param expiresIn the token validity duration in seconds
     * @throws IllegalArgumentException if token is null or expiresIn is negative
     */
    @Builder
    public SignInResponse(String token, Long expiresIn) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }
        if (expiresIn != null && expiresIn < 0) {
            throw new IllegalArgumentException("ExpiresIn cannot be negative");
        }
        this.token = token;
        this.expiresIn = expiresIn;
    }
}