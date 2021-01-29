package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.gladiators.exceptions.SubCategoryNotFoundException;
import project.gladiators.model.entities.SubCategory;
import project.gladiators.repository.SubCategoryRepository;
import project.gladiators.service.SubCategoryService;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static project.gladiators.constants.ExceptionMessages.SUBCATEGORY_NOT_FOUND;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final ModelMapper modelMapper;


    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository, ModelMapper modelMapper) {
        this.subCategoryRepository = subCategoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void seedSubCategory(SubCategoryServiceModel subCategoryServiceModel) {
        if (this.subCategoryRepository.findByName(subCategoryServiceModel.getName()) == null) {
            SubCategory subCategory = this.modelMapper.map(subCategoryServiceModel,SubCategory.class);
            subCategory.setEmpty(true);
            this.subCategoryRepository.saveAndFlush(subCategory);
        }
    }

    @Override
    public void editSubCategory(String id, SubCategoryServiceModel model) {
        SubCategory subCategory = this.subCategoryRepository.findById(id)
                .orElseThrow(()-> new SubCategoryNotFoundException(SUBCATEGORY_NOT_FOUND));
        subCategory.setName(model.getName());
        this.subCategoryRepository.save(subCategory);
    }

    @Override
    public List<SubCategoryServiceModel> allSubCategories() {
        return this.subCategoryRepository.findAll().stream()
                .map(c -> this.modelMapper.map(c, SubCategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public SubCategoryServiceModel findById(String id) {

        SubCategory subCategory = this.subCategoryRepository.findById(id)
                .orElseThrow(() -> new SubCategoryNotFoundException(SUBCATEGORY_NOT_FOUND));

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
