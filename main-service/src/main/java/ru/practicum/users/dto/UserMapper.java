package ru.practicum.users.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.users.model.User;

@UtilityClass
public class UserMapper {
    public static UserDto userToDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User dtoToUser(UserDto user) {
        return User.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto fromUSerToShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
