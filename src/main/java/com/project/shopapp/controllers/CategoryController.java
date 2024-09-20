package com.project.shopapp.controllers;

import com.project.shopapp.dtos.requests.CategoryDTO;
import com.project.shopapp.dtos.responses.CategoryResponse;
import com.project.shopapp.models.Category;
import com.project.shopapp.services.CategoryService;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    CategoryResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(
                                    MessageKeys.CREATE_CATEGORY_FAILED, errorMessages
                            ))
                            .build()
            );
        }
        Category category = Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        Category categoryResponse = categoryService.createCategory(category);
        return ResponseEntity.ok(
                CategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.CREATE_CATEGORY_SUCCESS))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @Valid @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    CategoryResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(
                                    MessageKeys.CREATE_CATEGORY_FAILED, errorMessages
                            ))
                            .build()
            );
        }
        Category category = Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        Category categoryResponse = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(
                CategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESS))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@Valid @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                CategoryResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY))
                        .build()
        );
    }
}
