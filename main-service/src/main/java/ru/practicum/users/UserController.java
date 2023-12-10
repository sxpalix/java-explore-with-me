package ru.practicum.users;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> get(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        log.info("\nGET [http://localhost:8080/admin/users] : запрос на просмотр пользователей\n");
        return service.getAllUsers(from, size,  ids);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody UserDto userDto) {
        log.info("\nPOST [http://localhost:8080/admin/users] : запрос на создание пользователя {}\n", userDto);
        return service.postUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId) {
        log.info("\nDELETE [http://localhost:8080/admin/users] : запрос на удаление пользователя {}\n", userId);
        service.deleteUser(userId);
    }
}
