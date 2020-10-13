package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {
    Category findByName(String name);
}
