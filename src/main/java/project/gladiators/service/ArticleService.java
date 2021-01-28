package project.gladiators.service;

import org.springframework.web.multipart.MultipartFile;
import project.gladiators.service.serviceModels.ArticleServiceModel;

import java.io.IOException;
import java.util.List;

public interface ArticleService {

    List<ArticleServiceModel> getAllArticles();

    ArticleServiceModel findArticleById(String id);

    void deleteById(String id);

    void registerArticle(ArticleServiceModel articleServiceModel, String username, MultipartFile image) throws IOException;
}
