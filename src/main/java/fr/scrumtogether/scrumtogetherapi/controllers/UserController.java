package fr.scrumtogether.scrumtogetherapi.controllers;

import fr.scrumtogether.scrumtogetherapi.dtos.UserDto;
import fr.scrumtogether.scrumtogetherapi.entities.User;
import fr.scrumtogether.scrumtogetherapi.mappers.UserMapper;
import fr.scrumtogether.scrumtogetherapi.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<PagedModel<UserDto>> getAll(@RequestParam @Nullable Integer page, @RequestParam @Nullable Integer size) {
        Page<User> paginated = userService.getAll(page, size);
        PagedModel<UserDto> paginatedDto = pagedResourcesAssembler.toModel(paginated, userMapper);
        return new ResponseEntity<>(paginatedDto, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        UserDto dto = userMapper.toModel(user);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userService.update(id, userDto);
        UserDto dto = userMapper.toModel(user);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
