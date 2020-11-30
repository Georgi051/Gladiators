package project.gladiators.service;

import project.gladiators.service.serviceModels.CategoryServiceModel;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;

import java.util.List;

public interface SubCategoryService {
    List<SubCategoryServiceModel> allSubCategories();

    List<SubCategoryServiceModel> findAll();

    SubCategoryServiceModel findById(String id);
}
