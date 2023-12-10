package ru.practicum.categories.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoryAdminService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryAdminService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("\nPOST [http://localhost:8080/categories] : запрос на добавление категории {}\n", categoryDto);
        return service.postCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patch(
            @PathVariable long catId,
            @Valid @RequestBody CategoryDto categoryDto
    ) {
        log.info("\nPATCH [http://localhost:8080/categories/{}] : запрос на обновление категории {} c ID {}\n", catId, categoryDto, catId);
        return service.patchCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        log.info("\nDELETE [http://localhost:8080/categories/{}] : запрос на удаление категории c ID {}\n", catId, catId);
        service.deleteCategory(catId);
    }
}
