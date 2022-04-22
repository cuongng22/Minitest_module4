package com.codegym.service.category;

import com.codegym.model.Category;
import com.codegym.service.IGeneralService;

import java.util.List;

public interface ICategoryService extends IGeneralService<Category> {
    void deleteCategory(Long id);

    List<Category> findALl();
}
