package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article,String> {
}
