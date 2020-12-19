package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.service.CategoryService;
import project.gladiators.service.OfferService;
import project.gladiators.service.SubCategoryService;
import project.gladiators.service.serviceModels.OfferServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;
import project.gladiators.web.viewModels.CategoryViewModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/subcategories")
public class SubCategoryController extends BaseController{

    private final SubCategoryService subCategoryService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final OfferService offerService;

    public SubCategoryController(SubCategoryService subCategoryService, CategoryService categoryService, ModelMapper modelMapper, OfferService offerService) {
        this.subCategoryService = subCategoryService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.offerService = offerService;
    }

    @GetMapping("/{id}")
    @PageTitle("Shop")
    public ModelAndView getSubCategoryProductsById(@PathVariable("id") String id,
                                                   ModelAndView modelAndView) {

        SubCategoryServiceModel subCategory = this.subCategoryService.findById(id);
        modelAndView.addObject("products", subCategory.getProducts());
        modelAndView.addObject("categories", this.categoryService.allCategories().stream()
                .map(c -> this.modelMapper.map(c, CategoryViewModel.class)).collect(Collectors.toList()));
        modelAndView.addObject("offers", this.offerService.findAllOffers());
        return view("product/products-by-subcategory", modelAndView);
    }

}
