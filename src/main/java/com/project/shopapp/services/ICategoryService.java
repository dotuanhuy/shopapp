package com.project.shopapp.services;

import com.project.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(Category category);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long id, Category category);
    void deleteCategory(long id);
}
