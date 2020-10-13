package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.bindingModels.ArticleRegisterBindingModel;
import project.gladiators.model.bindingModels.CategoryBindingModel;
import project.gladiators.service.ArticleService;
import project.gladiators.service.CategoryService;
import project.gladiators.service.CloudinaryService;
import project.gladiators.service.serviceModels.ArticleServiceModel;
import project.gladiators.service.serviceModels.CategoryServiceModel;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/moderators")
public class ModeratorController extends BaseController {
    private final CloudinaryService cloudinaryService;
    private final CategoryService categoryService;
    private final ArticleService articleService;
    private final ModelMapper modelMapper;

    @Autowired
    public ModeratorController(CloudinaryService cloudinaryService, CategoryService categoryService, ArticleService articleService, ModelMapper modelMapper) {
        this.cloudinaryService = cloudinaryService;
        this.categoryService = categoryService;
        this.articleService = articleService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/article-add")
    @PageTitle("Add article")
    public ModelAndView addArticle(ModelAndView modelAndView) {

        modelAndView.addObject("article", new ArticleRegisterBindingModel());

        return super.view("/moderator/article-add",
                modelAndView);
    }

    @PostMapping("/article-add")
    public ModelAndView addArticle(@Valid @ModelAttribute("article") ArticleRegisterBindingModel articleRegisterBindingModel, BindingResult result, Principal principal
            , RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("article", articleRegisterBindingModel);
            modelAndView.addObject("org.springframework.validation.BindingResult.article", result);
            return super.view("/moderator/article-add", modelAndView);
        }
        String imageUrl = articleRegisterBindingModel.getImage().isEmpty() ? "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg"
                : this.cloudinaryService.uploadImageToCurrentFolder(articleRegisterBindingModel.getImage(), "articles");
        ArticleServiceModel articleServiceModel = modelMapper.map(articleRegisterBindingModel, ArticleServiceModel.class);
        articleServiceModel.setImageUrl(imageUrl);

        articleService.registerArticle(articleServiceModel, principal.getName());
        redirectAttributes.addFlashAttribute("statusMessage", "You created article successful");
        redirectAttributes.addFlashAttribute("statusCode", "successful");
        return super.redirect("/moderators/article-add");
    }


    @GetMapping("/category-add")
    @PageTitle("Add category")
    public ModelAndView addCategory(ModelAndView modelAndView) {
        modelAndView.addObject("category",new CategoryBindingModel());
        return super.view("/moderator/category-add",modelAndView);
    }

    @PostMapping("/category-add")
    public ModelAndView addCategory(@Valid  @ModelAttribute(name = "category")CategoryBindingModel categoryBindingModel,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    ModelAndView modelAndView) {
        if (result.hasErrors()){
            modelAndView.addObject("category",categoryBindingModel);
            return super.view("/moderator/category-add", modelAndView);
        }

        this.categoryService.seedCategoryInDb(this.modelMapper.map(categoryBindingModel, CategoryServiceModel.class));

        redirectAttributes.addFlashAttribute("statusMessage", "You created category successfully");
        redirectAttributes.addFlashAttribute("statusCode", "successful");
        return super.redirect("/categories/all");
    }

}
