package project.gladiators.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.gladiators.model.entities.Article;
import project.gladiators.model.entities.User;
import project.gladiators.repository.ArticleRepository;
import project.gladiators.repository.UserRepository;

import java.time.LocalDateTime;

@Component
@Order(value = 5)
public class ArticleInitialization implements CommandLineRunner {
    private ArticleRepository articleRepository;
    private UserRepository userRepository;

    @Autowired
    public ArticleInitialization(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        if (articleRepository.count() == 0) {
//            User user = userRepository.findAll().get(0);
//            Article article = new Article("Test", "This is a test article.Everything is working..maybe", LocalDateTime.now(), "https://cdn.pixabay.com/photo/2014/09/25/23/36/man-461195_1280.jpg"
//                    , user);
//            articleRepository.save(article);
//        }
    }
}
