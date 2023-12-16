package ru.practicum.categories.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.model.Category;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category model) {
        return CategoryDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    public static Category fromDto(CategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }
}
