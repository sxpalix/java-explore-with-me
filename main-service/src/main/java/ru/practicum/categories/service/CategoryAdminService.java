package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;

public interface CategoryAdminService {
    CategoryDto postCategory(CategoryDto categoryDto);

    CategoryDto patchCategory(long catId, CategoryDto categoryDto);

    void deleteCategory(long catId);
}
