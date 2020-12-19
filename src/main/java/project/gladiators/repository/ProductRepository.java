package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Product;
import project.gladiators.model.entities.SubCategory;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    Product findByName(String name);

}
