package ru.practicum.categories.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class CategoryAdminServiceImpl implements CategoryAdminService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto postCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.fromDto(categoryDto);
        Category saveCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(saveCategory);
    }

    @Override
    @Transactional
    public CategoryDto patchCategory(long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d not found", catId)));

        Optional.ofNullable(categoryDto.getName()).ifPresent(category::setName);
        Category saveCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(saveCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d not found", catId)));
        categoryRepository.deleteById(catId);
    }
}
