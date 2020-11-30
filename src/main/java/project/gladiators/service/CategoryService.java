package project.gladiators.service;


import project.gladiators.model.entities.Category;
import project.gladiators.service.serviceModels.CategoryServiceModel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategoryInDb(CategoryServiceModel categoryServiceModel);

    List<CategoryServiceModel> allCategories();

    CategoryServiceModel findCategory(String id);

    void editCategory(String id, CategoryServiceModel model);

    void deleteCategory(String id);

    List<Category> findAll();
}
