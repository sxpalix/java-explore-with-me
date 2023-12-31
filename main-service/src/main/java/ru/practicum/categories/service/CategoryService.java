package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto getById(long catId);

    List<CategoryDto> getAll(int from, int size);

    CategoryDto postCategory(CategoryDto categoryDto);

    CategoryDto patchCategory(long catId, CategoryDto categoryDto);

    void deleteCategory(long catId);
}
