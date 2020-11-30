package project.gladiators.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gladiators.model.entities.Category;
import project.gladiators.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryRestController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryRestController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/categories/all")
    public List<Category> findAllCategories(){

        return this.categoryService.findAll();
    }
}
