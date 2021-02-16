package project.gladiators.integrational.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.entities.Article;
import project.gladiators.model.entities.User;
import project.gladiators.repository.ArticleRepository;
import project.gladiators.repository.UserRepository;
import project.gladiators.service.ArticleService;
import project.gladiators.service.serviceModels.ArticleServiceModel;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {

    Article article;
    Article article1;
    List<Article> articles;
    MultipartFile multipartFile;
    User user;

    @MockBean
    ArticleRepository articleRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        article = new Article();
        article.setId("1");
        article.setAddedOn(LocalDateTime.now());
        article.setTitle("Test");
        article.setDescription("Test");
        user = new User();
        user.setUsername("Username");
        user.setId("1");
        article.setUser(user);
        multipartFile = new MockMultipartFile("picture",
                "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg",
                IMAGE_JPEG.getMimeType(), (byte[]) null);
        article1 = new Article();
        articles = new ArrayList<>();
        articles.add(article);
        articles.add(article1);
    }

    @Test
    public void getAllArticles_shouldReturnCollectionOfAllArticles(){
        when(articleRepository.findAll())
                .thenReturn(articles);
        List<ArticleServiceModel> articleServiceModels = this.articleService.getAllArticles();
        Assertions.assertEquals(articleRepository.findAll().size(), articleServiceModels.size());
    }

    @Test
    public void findArticleById_shouldReturnCorrectArticle(){
        when(articleRepository.findById("1"))
                .thenReturn(Optional.of(article));
        ArticleServiceModel articleServiceModel = articleService.findArticleById("1");
        Assertions.assertEquals(article.getTitle(), articleServiceModel.getTitle());

    }

    @Test
    public void deleteById_shouldDeleteArticle(){
        Mockito.when(articleRepository.findById("1"))
                .thenReturn(Optional.of(article));
        articleService.deleteById("1");
        Mockito.verify(articleRepository)
                .deleteById("1");
    }

    @Test
    public void registerArticle_shouldCreateArticle() throws IOException {
        when(userRepository.findUserByUsername("Username"))
                .thenReturn(Optional.of(user));
        articleService.registerArticle(modelMapper.map(article, ArticleServiceModel.class), "Username", multipartFile);
        verify(articleRepository)
                .save(any());
    }
}
