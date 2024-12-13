package fr.scrumtogether.scrumtogetherapi.services;

import fr.scrumtogether.scrumtogetherapi.dtos.UserDto;
import fr.scrumtogether.scrumtogetherapi.entities.User;
import fr.scrumtogether.scrumtogetherapi.exceptions.EntityNotFoundException;
import fr.scrumtogether.scrumtogetherapi.mappers.UserMapper;
import fr.scrumtogether.scrumtogetherapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public Page<User> getAll(Integer pageNumber, Integer pageSize) {
        log.debug("Getting all users - paginated");

        // Validate and set default page number
        int validatedPageNumber = Optional.ofNullable(pageNumber)
                .filter(page -> page > 0)
                .orElse(0);

        // Validate and set default page size with upper bound
        int validatedPageSize = Optional.ofNullable(pageSize)
                .filter(size -> size > 0 && size <= 100)
                .orElse(20);

        log.debug("Using page number: {}, page size: {}", validatedPageNumber, validatedPageSize);

        PageRequest pageRequest = PageRequest.of(validatedPageNumber, validatedPageSize);
        return userRepository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public User update(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userMapper.updateEntity(user, userDto);
        return user;
    }

    @Transactional
    public User update(Long userId, UserDto userDto) {
        if (!userId.equals(userDto.getId())) {
            throw new IllegalArgumentException("User id in path and body must be the same");
        }

        return update(userDto);
    }

    @Transactional
    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }
}
