package ru.practicum.categories.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto getById(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d not found", catId)));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from, size))
                .getContent().stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
