package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.service.ArticleService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.ArticleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.web.viewModels.ArticleViewModel;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController extends BaseController {

    private ArticleService articleService;
    private ModelMapper modelMapper;

    @Autowired
    public ArticleController(ArticleService articleService, ModelMapper modelMapper) {
        this.articleService = articleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ModelAndView getAllArticles() {
        List<ArticleViewModel> articles = Arrays.asList(this.modelMapper.map(articleService.getAllArticles(), ArticleViewModel[].class));
        return super.view("articles", new ModelAndView().addObject("articles",articles));
    }

    @GetMapping("{id}")
    public ModelAndView getSpecificArticle(@PathVariable String id){
        ArticleServiceModel articleServiceModel = articleService.findArticleById(id);
        ArticleViewModel article=this.modelMapper.map(articleServiceModel, ArticleViewModel.class);
        article.setUserServiceModelUsername(articleServiceModel.getUserServiceModel().getUsername());
        return super.view("article-info", new ModelAndView().addObject("article",article));
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public ModelAndView deleteSpecificArticle(@PathVariable String id){
        articleService.deleteById(id);
        return super.redirect("/articles");
    }
}
