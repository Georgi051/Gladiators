package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.SubCategory;
import project.gladiators.repository.CategoryRepository;
import project.gladiators.repository.SubCategoryRepository;
import project.gladiators.service.SubCategoryService;
import project.gladiators.service.serviceModels.CategoryServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<SubCategoryServiceModel> allSubCategories() {
        return this.subCategoryRepository.findAll().stream()
                .map(c -> this.modelMapper.map(c, SubCategoryServiceModel.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<SubCategoryServiceModel> findAll() {

        List<SubCategoryServiceModel> subCategoryServiceModels = new ArrayList<>();

        this.subCategoryRepository.findAll()
                .forEach(subCategory -> {
                    subCategoryServiceModels.add(this.modelMapper
                    .map(subCategory, SubCategoryServiceModel.class));
                });
        return subCategoryServiceModels;
    }

    @Override
    public SubCategoryServiceModel findById(String id) {

        SubCategory subCategory = this.subCategoryRepository.findById(id)
                .orElse(null);
        SubCategoryServiceModel subCategoryServiceModel = this.modelMapper
                .map(subCategory, SubCategoryServiceModel.class);
        subCategoryServiceModel.setProducts(new HashSet<>());
        subCategory.getProducts().forEach(product -> {
            ProductServiceModel productServiceModel = this.modelMapper
                    .map(product, ProductServiceModel.class);
            subCategoryServiceModel.getProducts().add(productServiceModel);
        });
        return subCategoryServiceModel;
    }
}
