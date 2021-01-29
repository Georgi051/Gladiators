package project.gladiators.service;

import project.gladiators.service.serviceModels.SubCategoryServiceModel;

import java.util.List;

public interface SubCategoryService {
    List<SubCategoryServiceModel> allSubCategories();

    SubCategoryServiceModel findById(String id);

    void seedSubCategory(SubCategoryServiceModel subCategoryServiceModel);

    void editSubCategory(String id, SubCategoryServiceModel model);
}
