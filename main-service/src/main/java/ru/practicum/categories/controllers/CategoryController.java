package ru.practicum.categories.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        log.info("\nGET [http://localhost:8080/categories] : запрос на просмотр категорий\n");
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable long catId) {
        log.info("\nGET [http://localhost:8080/categories/{}] : запрос на просмотр категории по ID {}\n", catId, catId);
        return categoryService.getById(catId);
    }
}
