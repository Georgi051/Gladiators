package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.bindingModels.SubCategoryBindingModel;
import project.gladiators.service.CategoryService;
import project.gladiators.service.OfferService;
import project.gladiators.service.SubCategoryService;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;
import project.gladiators.validators.moderator.AddSubCategoryValidator;
import project.gladiators.web.viewModels.CategoryViewModel;
import project.gladiators.web.viewModels.SubCategoryViewModel;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/subcategories")
public class SubCategoryController extends BaseController {
    private final SubCategoryService subCategoryService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final AddSubCategoryValidator subCategoryValidator;
    private final OfferService offerService;

    public SubCategoryController(SubCategoryService subCategoryService, CategoryService categoryService, ModelMapper modelMapper, AddSubCategoryValidator subCategoryValidator, OfferService offerService) {
        this.subCategoryService = subCategoryService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.subCategoryValidator = subCategoryValidator;
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

    @GetMapping("/subCategory-add")
    @PageTitle("Add sub-category")
    @PreAuthorize("hasRole('MODERATOR')")
    public ModelAndView addCategory(ModelAndView modelAndView) {
        modelAndView.addObject("subCategory", new SubCategoryBindingModel());
        return super.view("/subCategory/subCategory-add", modelAndView);
    }

    @PostMapping("/subCategory-add")
    public ModelAndView addCategory(@Valid @ModelAttribute(name = "subCategory") SubCategoryBindingModel subCategoryBindingModel,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    ModelAndView modelAndView) {
        subCategoryValidator.validate(subCategoryBindingModel,result);
        if (result.hasErrors()) {
            modelAndView.addObject("subCategory", subCategoryBindingModel);
            return super.view("/subCategory/subCategory-add", modelAndView);
        }

        this.subCategoryService.seedSubCategory(this.modelMapper.map(subCategoryBindingModel, SubCategoryServiceModel.class));

        redirectAttributes.addFlashAttribute("statusMessage", "You created category successfully");
        redirectAttributes.addFlashAttribute("statusCode", "successful");

        return super.redirect("/subcategories/all");
    }

    @GetMapping("/all")
    @PageTitle("All subcategories")
    @PreAuthorize("hasRole('MODERATOR')")
    public ModelAndView allCategories(ModelAndView modelAndView) {
        modelAndView.addObject("subCategories", this.subCategoryService.allSubCategories().stream()
                .map(subCategoryServiceModel -> this.modelMapper.map(subCategoryServiceModel, SubCategoryViewModel.class))
                .sorted(Comparator.comparing(SubCategoryViewModel::getName))
                .collect(Collectors.toList()));
        return super.view("/subCategory/all-subCategories", modelAndView);
    }

    @GetMapping("edit/{id}")
    public ModelAndView editSubcategory(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("subCategory",
                this.modelMapper.map(this.subCategoryService.findById(id), SubCategoryViewModel.class));
        modelAndView.addObject("subcategoryId",id);
        return super.view("/subCategory/edit-subCategory", modelAndView);
    }

    @PostMapping("edit/{id}")
    public ModelAndView editSubcategory(@PathVariable String id, @Valid @ModelAttribute(name = "subCategory") SubCategoryBindingModel subCategory,
                                            BindingResult result,
                                            ModelAndView modelAndView) {
        subCategoryValidator.validate(subCategory,result);
        if (result.hasErrors()){
            modelAndView.addObject("subcategoryId",id);
            modelAndView.addObject("subCategory", subCategory);
            return super.view("/subCategory/edit-subCategory", modelAndView);
        }

        this.subCategoryService.editSubCategory(id, this.modelMapper.map(subCategory, SubCategoryServiceModel.class));
        return super.redirect("/subcategories/all");
    }

}
