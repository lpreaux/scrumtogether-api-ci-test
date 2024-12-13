package fr.scrumtogether.scrumtogetherapi.mappers;

import fr.scrumtogether.scrumtogetherapi.controllers.UserController;
import fr.scrumtogether.scrumtogetherapi.dtos.RegistrationDto;
import fr.scrumtogether.scrumtogetherapi.dtos.UserDto;
import fr.scrumtogether.scrumtogetherapi.entities.User;
import fr.scrumtogether.scrumtogetherapi.entities.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Component
public class UserMapper extends RepresentationModelAssemblerSupport<User, UserDto> {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        super(UserController.class, UserDto.class);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Converts a registration DTO to a User entity with encoded password.
     * Also performs data sanitization and validation.
     *
     * @param registrationDto the registration details
     * @return the mapped User entity
     */
    public User toEntity(RegistrationDto registrationDto) {
        log.debug("Mapping registration request for user: {}", registrationDto.getUsername());

        return User.builder()
                .lastName(registrationDto.getLastName().trim())
                .firstName(registrationDto.getFirstName().trim())
                .email(registrationDto.getEmail().trim().toLowerCase())
                .username(registrationDto.getUsername().trim())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .verifiedEmail(false)
                .role(Role.DEFAULT)
                .build();
    }

    public void updateEntity(User user, UserDto userDto) {
        log.debug("Updating user: {}", user.getUsername());

        user.setLastName(userDto.getLastName());
        user.setFirstName(userDto.getFirstName());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setVerifiedEmail(userDto.getVerifiedEmail());
        user.setRole(userDto.getRole());
    }

    @Override
    @NonNull
    public UserDto toModel(@NonNull User entity) {
        log.debug("Mapping user to user DTO: {}", entity.getUsername());

        UserDto userDto = instantiateModel(entity);
        userDto.setId(entity.getId());
        userDto.setLastName(entity.getLastName());
        userDto.setFirstName(entity.getFirstName());
        userDto.setEmail(entity.getEmail());
        userDto.setUsername(entity.getUsername());
        userDto.setVerifiedEmail(entity.getVerifiedEmail());
        userDto.setRole(entity.getRole());
        userDto.setVersion(entity.getVersion());
        userDto.setCreatedAt(entity.getCreatedAt());
        userDto.setUpdatedAt(entity.getUpdatedAt());

        userDto.add(linkTo(methodOn(UserController.class).getById(entity.getId())).withSelfRel());
        return userDto;
    }
}
