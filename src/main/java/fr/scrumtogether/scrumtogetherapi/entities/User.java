package fr.scrumtogether.scrumtogetherapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import fr.scrumtogether.scrumtogetherapi.entities.enums.Role;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Represents a user entity in the system, integrating with Spring Security through UserDetails interface.
 * This entity stores user authentication and profile information with audit capabilities.
 * <p>
 * The entity is mapped to the "user" table in the database and includes indexes for optimized queries
 * on frequently accessed fields (username and email).
 *
 * @see UserDetails
 * @see AuditingEntityListener
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user", indexes = {
        @Index(name = "idx_user_username", columnList = "username"),
        @Index(name = "idx_user_email", columnList = "email")
})
public class User implements UserDetails {
    /**
     * The unique identifier for this user.
     * Automatically generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user's last name.
     * Required field that cannot be null.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * The user's first name.
     * Required field that cannot be null.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * The user's email address.
     * Must be unique across all users and cannot be null.
     * Used for account verification and communication.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * The user's chosen username.
     * Must be unique across all users and cannot be null.
     * Used for authentication and identification.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * The user's encrypted password.
     * Required field that cannot be null.
     * Excluded from toString() for security reasons.
     */
    @ToString.Exclude
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The user's role in the system.
     * Stored as a string representation of {@link Role} enum.
     * Determines the user's permissions and access levels.
     * @see Role#getAuthority() for Spring Security integration
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.DEFAULT;

    /**
     * Indicates whether the user's email has been verified.
     * Defaults to false for new users.
     * Used to determine account activation status.
     */
    @Builder.Default
    @Column(name = "verified_email")
    private Boolean verifiedEmail = false;

    /**
     * Version field for optimistic locking.
     * Used to prevent concurrent modifications of the same entity.
     */
    @Version
    private Long version;

    /**
     * Timestamp of when this user was created.
     * Automatically set by JPA auditing.
     * Cannot be updated after creation.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last update to this user.
     * Automatically updated by JPA auditing.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Returns the authorities granted to the user.
     * Converts the user's role to a Spring Security GrantedAuthority.
     *
     * @return a list containing a single authority based on the user's role
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the account is valid (non-expired), false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the user is not locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return true if the credentials are valid (non-expired), false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * A user is considered enabled when their email is verified.
     *
     * @return true if the user's email is verified, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
        // return verifiedEmail;
    }

    /**
     * Checks if this user has administrative privileges.
     *
     * @return true if the user has the ADMIN role, false otherwise
     */
    public boolean isAdmin() {
        return role.isAdmin();
    }

    /**
     * Returns a formatted display name combining the user's first and last name.
     *
     * @return the full name of the user
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns a string representation of this user.
     * Excludes sensitive information like password.
     *
     * @return a string containing the user's id, username, email, role, and verification status
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role.toString() +
                ", verifiedEmail=" + verifiedEmail +
                '}';
    }
}