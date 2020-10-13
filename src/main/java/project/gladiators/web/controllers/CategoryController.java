package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.bindingModels.CategoryBindingModel;
import project.gladiators.service.CategoryService;
import project.gladiators.service.serviceModels.CategoryServiceModel;
import project.gladiators.web.viewModels.CategoryViewModel;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categories")
public class CategoryController extends BaseController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/category-add")
    @PageTitle("Add category")
    public ModelAndView addCategory(ModelAndView modelAndView) {
        modelAndView.addObject("category",new CategoryBindingModel());
        return super.view("/category/category-add",modelAndView);
    }

    @PostMapping("/category-add")
    public ModelAndView addCategory(@Valid  @ModelAttribute(name = "category")CategoryBindingModel categoryBindingModel,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    ModelAndView modelAndView) {
        if (result.hasErrors()){
            modelAndView.addObject("category",categoryBindingModel);
            return super.view("/category/category-add", modelAndView);
        }

        this.categoryService.seedCategoryInDb(this.modelMapper.map(categoryBindingModel, CategoryServiceModel.class));

        redirectAttributes.addFlashAttribute("statusMessage", "You created category successfully");
        redirectAttributes.addFlashAttribute("statusCode", "successful");
        return super.redirect("/categories/all");
    }

    @GetMapping("/all")
    @PageTitle("All categories")
    public ModelAndView allCategories(ModelAndView modelAndView) {
        modelAndView.addObject("categories", this.categoryService.allCategories().stream()
                .map(c -> this.modelMapper.map(c, CategoryViewModel.class)).collect(Collectors.toList()));
        return super.view("/category/all-categories", modelAndView);
    }

    @GetMapping("edit/{id}")
    public ModelAndView editCategory(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("category",
                this.modelMapper.map(this.categoryService.findCategory(id), CategoryViewModel.class));
        modelAndView.addObject("categoryId",id);
        return super.view("/category/edit-category", modelAndView);
    }

    @PostMapping("edit/{id}")
    public ModelAndView confirmEditCategory(@PathVariable String id, @Valid @ModelAttribute(name = "category") CategoryBindingModel category,
                                            BindingResult result,
                                            ModelAndView modelAndView) {
        if (result.hasErrors()){
            modelAndView.addObject("categoryId",id);
            modelAndView.addObject("category", category);
            return super.view("/category/edit-category", modelAndView);
        }

        this.categoryService.editCategory(id, this.modelMapper.map(category, CategoryServiceModel.class));
        return super.redirect("/categories/all");
    }

    @GetMapping("delete/{id}")
    public ModelAndView deleteCategory(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("category",
                this.modelMapper.map(this.categoryService.findCategory(id), CategoryViewModel.class));
        return super.view("/category/delete-category", modelAndView);
    }

    @PostMapping("delete/{id}")
    public ModelAndView deleteCategory(@PathVariable String id) {
        this.categoryService.deleteCategory(id);
        return super.redirect("/categories/all");
    }
}
