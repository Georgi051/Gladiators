package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.constants.ExceptionMessages;
import project.gladiators.exceptions.ArticleNotFoundException;
import project.gladiators.model.entities.Article;
import project.gladiators.repository.ArticleRepository;
import project.gladiators.service.ArticleService;
import project.gladiators.service.CloudinaryService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.ArticleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static project.gladiators.constants.ExceptionMessages.ARTICLE_NOT_FOUND;

@Service
public class ArticleServiceImpl implements ArticleService {
    private ArticleRepository articleRepository;
    private ModelMapper modelMapper;
    private CloudinaryService cloudinaryService;
    private UserService userService;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ModelMapper modelMapper, CloudinaryService cloudinaryService, UserService userService) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.userService = userService;
    }

    @Override
    public List<ArticleServiceModel> getAllArticles() {
        return Arrays.asList(this.modelMapper.map(articleRepository.findAll(), ArticleServiceModel[].class));
    }

    @Override
    public ArticleServiceModel findArticleById(String id) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(ARTICLE_NOT_FOUND));
        ArticleServiceModel articleServiceModel = this.modelMapper
                .map(article, ArticleServiceModel.class);
        articleServiceModel.setUserServiceModel(this.modelMapper
                .map(article.getUser(), UserServiceModel.class));
        return articleServiceModel;
    }

    @Override
    public void deleteById(String id) {
        articleRepository.deleteById(id);
    }

    @Override
    public void registerArticle(ArticleServiceModel articleServiceModel, String username, MultipartFile articleImage) throws IOException {
        UserServiceModel user = userService.findUserByUsername(username);
        String imageUrl = articleImage.isEmpty() ? "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg"
                : this.cloudinaryService.uploadImageToCurrentFolder(articleImage, "articles");
        articleServiceModel.setUserServiceModel(user);
        articleServiceModel.setAddedOn(LocalDateTime.now());
        articleServiceModel.setImageUrl(imageUrl);
        articleRepository.save(this.modelMapper.map(articleServiceModel, Article.class));
    }
}
