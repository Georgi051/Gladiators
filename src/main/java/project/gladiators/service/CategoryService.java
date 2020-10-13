package project.gladiators.service;


import project.gladiators.service.serviceModels.CategoryServiceModel;

import java.util.List;

public interface CategoryService {
    void seedCategoryInDb(CategoryServiceModel categoryServiceModel);

    List<CategoryServiceModel> allCategories();

    CategoryServiceModel findCategory(String id);

    void editCategory(String id, CategoryServiceModel model);

    void deleteCategory(String id);

}
