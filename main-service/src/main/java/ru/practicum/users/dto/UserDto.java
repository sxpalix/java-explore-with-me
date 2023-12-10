package ru.practicum.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Validated
public class UserDto {
    @NotBlank(message = "Field: name. Error: must not be blank. Value: null") @Size(min = 2, max = 250)
    private String name;
    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Email(message = "Email should be format: practicum@yandex.ru")
    @Size(min = 6, max = 254)
    private String email;
}
