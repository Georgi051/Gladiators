package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import project.gladiators.annotations.PageTitle;
import project.gladiators.service.CategoryService;
import project.gladiators.service.OfferService;
import project.gladiators.service.ProductService;
import project.gladiators.web.viewModels.CategoryViewModel;
import project.gladiators.web.viewModels.Product;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ShopController extends BaseController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final OfferService offerService;

    public ShopController(CategoryService categoryService, ModelMapper modelMapper, ProductService productService, OfferService offerService) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.offerService = offerService;
    }

    @GetMapping("/shop")
    @PageTitle("Shop")
    public ModelAndView getAllCategories(ModelAndView modelAndView) {
        List<Product> products = this.productService.allProducts()
                .stream()
                .filter(product -> !product.isDeleted())
                .map(p -> this.modelMapper.map(p, Product.class))
                .collect(Collectors.toList());
        modelAndView.addObject("categories", this.categoryService.allCategories().stream()
                .map(c -> this.modelMapper.map(c, CategoryViewModel.class)).collect(Collectors.toList()));
        modelAndView.addObject("products", products);
        Set<String> manufacturers = new LinkedHashSet<>();
        products.forEach(product -> {
            manufacturers.add(product.getManufacturerName());
        });
        modelAndView.addObject("manufacturers", manufacturers);
        modelAndView.addObject("offers", this.offerService.findAllOffers());
        return super.view("/shop", modelAndView);
    }
}
