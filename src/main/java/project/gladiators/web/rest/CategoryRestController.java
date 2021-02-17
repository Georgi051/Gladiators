package project.gladiators.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gladiators.model.entities.Category;
import project.gladiators.model.entities.SubCategory;
import project.gladiators.service.CategoryService;
import project.gladiators.service.serviceModels.CategoryServiceModel;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryRestController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryServiceModel>> findAllCategories(){

        List<CategoryServiceModel> categoryServiceModels = categoryService.allCategories();

        return ResponseEntity.ok(categoryServiceModels);
    }
}
