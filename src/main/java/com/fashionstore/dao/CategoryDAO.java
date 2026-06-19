package com.fashionstore.dao;

import java.util.List;

import com.fashionstore.model.Category;

public interface CategoryDAO {
    List<Category> getAllCategories();
    Category getCategoryById(int id);
}
