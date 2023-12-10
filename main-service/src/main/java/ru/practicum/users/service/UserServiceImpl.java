package ru.practicum.users.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public List<User> getAllUsers(int from, int size, List<Long> ids) {
        return ids == null ? repository.findAll(PageRequest.of(from, size)).stream().collect(Collectors.toList()) :
                repository.findAllByIdIn(ids, PageRequest.of(from, size));
    }

    @Transactional
    public User postUser(UserDto user) {
        return repository.save(UserMapper.dtoToUser(user));
    }

    @Transactional
    public void deleteUser(long userId) {
        if (repository.findById(userId).isEmpty())
            throw new NotFoundException(String.format("User with id= %s was not found", userId));
         else repository.deleteById(userId);
    }
}
