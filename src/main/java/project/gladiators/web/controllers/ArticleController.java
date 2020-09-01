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
        ArticleViewModel article=this.modelMapper.map(this.articleService.findArticleById(id),ArticleViewModel.class);
        return super.view("article-info",new ModelAndView().addObject("article",article));
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public String deleteSpecificArticle(@PathVariable String id){

        return String.format("<h1>Delete  %s</h1>",id);
    }
}
