package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import project.gladiators.service.CategoryService;
import project.gladiators.service.CloudinaryService;
import project.gladiators.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController{
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }


}
