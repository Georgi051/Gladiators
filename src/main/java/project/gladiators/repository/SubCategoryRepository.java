package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Category;
import project.gladiators.model.entities.Product;
import project.gladiators.model.entities.SubCategory;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,String> {

    SubCategory findByName(String name);

    List<SubCategory> findAllByProducts(Product product);

}
