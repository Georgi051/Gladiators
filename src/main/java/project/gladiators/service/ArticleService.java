package project.gladiators.service;

import project.gladiators.service.serviceModels.ArticleServiceModel;

import java.util.List;

public interface ArticleService {

    List<ArticleServiceModel> getAllArticles();

    ArticleServiceModel findArticleById(String id);
}
