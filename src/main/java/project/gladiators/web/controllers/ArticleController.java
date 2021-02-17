package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.bindingModels.ArticleRegisterBindingModel;
import project.gladiators.service.ArticleService;
import project.gladiators.service.CloudinaryService;
import project.gladiators.service.serviceModels.ArticleServiceModel;
import project.gladiators.validators.moderator.AddArticleValidator;
import project.gladiators.web.viewModels.ArticleViewModel;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController extends BaseController {

    private final ArticleService articleService;
    private final ModelMapper modelMapper;
    private final AddArticleValidator addArticleValidator;

    @Autowired
    public ArticleController(ArticleService articleService, ModelMapper modelMapper, AddArticleValidator addArticleValidator) {
        this.articleService = articleService;
        this.modelMapper = modelMapper;
        this.addArticleValidator = addArticleValidator;
    }


    @GetMapping("/article-add")
    @PageTitle("Add article")
    @PreAuthorize("hasRole('MODERATOR')")
    public ModelAndView addArticle(ModelAndView modelAndView) {

        modelAndView.addObject("article", new ArticleRegisterBindingModel());

        return super.view("/article/article-add",
                modelAndView);
    }

    @PostMapping("/article-add")
    @PageTitle("Add article")
    public ModelAndView addArticle(@Valid @ModelAttribute("article") ArticleRegisterBindingModel articleRegisterBindingModel, BindingResult result, Principal principal
            , RedirectAttributes redirectAttributes) throws IOException {

        addArticleValidator.validate(articleRegisterBindingModel,result);
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("article", articleRegisterBindingModel);
            modelAndView.addObject("org.springframework.validation.BindingResult.article", result);
            return super.view("/article/article-add", modelAndView);
        }
        ArticleServiceModel articleServiceModel = modelMapper.map(articleRegisterBindingModel, ArticleServiceModel.class);
        articleService.registerArticle(articleServiceModel, principal.getName(),articleRegisterBindingModel.getImage());
        redirectAttributes.addFlashAttribute("statusMessage", "You created article successful");
        redirectAttributes.addFlashAttribute("statusCode", "successful");
        return super.redirect("/articles/article-add");
    }

    @GetMapping
    @PageTitle("All articles")
    public ModelAndView getAllArticles() {
        List<ArticleViewModel> articles = Arrays.asList(this.modelMapper.map(articleService.getAllArticles(), ArticleViewModel[].class));
        return super.view("/article/articles", new ModelAndView().addObject("articles",articles));
    }

    @GetMapping("{id}")
    @PageTitle("Article Info")
    public ModelAndView getSpecificArticle(@PathVariable String id){
        ArticleServiceModel articleServiceModel = articleService.findArticleById(id);
        ArticleViewModel article=this.modelMapper.map(articleServiceModel, ArticleViewModel.class);
        article.setUserServiceModelUsername(articleServiceModel.getUserServiceModel().getUsername());
        return super.view("/article/article-info", new ModelAndView().addObject("article",article));
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteSpecificArticle(@PathVariable String id){
        articleService.deleteById(id);
        return super.redirect("/articles");
    }
}
