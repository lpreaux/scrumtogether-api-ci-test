package scrumtogether.scrumtogetherapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import scrumtogether.scrumtogetherapi.entities.enums.Role;

import java.util.Collection;
import java.util.List;

/**
 * The User class represents a user entity in the system with essential attributes such as
 * username, email, password, and role. It implements the {@code UserDetails} interface for
 * integration with Spring Security.
 * <p>
 * This class is annotated with JPA and Lombok annotations to simplify
 * object-relational mapping and boilerplate code generation.
 * It is mapped to the "user" table in the database.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User implements UserDetails {
    /**
     * Represents the unique identifier for the User entity.
     * This field is automatically generated by the database using an identity strategy.
     * It is mapped to the "id" column in the database and cannot be null.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Represents the last name of the user.
     * This field is mapped to the "last_name" column in the database.
     * It is a required field and cannot be null.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * Represents the first name of the user.
     * This field is mapped to the "first_name" column in the database.
     * It is a required field and cannot be null.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Represents the email address of the user.
     * This field is mapped to the "email" column in the database.
     * It is a required field (non-nullable) and must be unique across all records.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Represents the username of the user.
     * This field is mapped to the "username" column in the database, and it is both required and unique.
     * It is a critical field for user identification and authentication.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Represents the password of the user. This variable is mapped to the "password" column
     * in the database. It is a required field, thus marked as non-nullable.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Represents the role assigned to the user in the system.
     * This variable is mapped to the "role" column in the database
     * and is stored as a string representation of the Enum {@code Role}.
     * <p>
     * The default value of this field is {@code Role.DEFAULT}.
     * It is marked as non-nullable to ensure every user has a role assigned.
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.DEFAULT;

    /**
     * Indicates whether the user's email address has been verified.
     * By default, this value is set to false.
     * This field is mapped to the "verified_email" column in the database and is unique.
     */
    @Builder.Default
    @Column(name = "verified_email", unique = true)
    private Boolean verified_email = false;

    /**
     * Retrieves the collection of GrantedAuthority objects that represents
     * the authorities granted to the user based on their role.
     *
     * @return a collection of GrantedAuthority objects containing the user's role as a SimpleGrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Returns a string representation of the User object.
     * The string includes the values of the id, username, email, and role fields.
     *
     * @return a string representation of the User object.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}