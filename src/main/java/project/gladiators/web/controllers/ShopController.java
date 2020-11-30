package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.service.CategoryService;
import project.gladiators.web.viewModels.CategoryViewModel;

import java.util.stream.Collectors;

@Controller
public class ShopController extends BaseController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public ShopController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/shop")
    @PageTitle("Shop")
    public ModelAndView getAllCategories(ModelAndView modelAndView) {
        modelAndView.addObject("categories", this.categoryService.allCategories().stream()
                .map(c -> this.modelMapper.map(c, CategoryViewModel.class)).collect(Collectors.toList()));
        return super.view("/shop", modelAndView);
    }
}
