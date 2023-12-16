package ru.practicum.users.service;

import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(int from, int size, List<Long> ids);

    User createUser(UserDto user);

    void deleteUser(long userId);
}
