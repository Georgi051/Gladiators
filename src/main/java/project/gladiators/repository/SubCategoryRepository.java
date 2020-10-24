package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,String> {
    SubCategory findByName(String name);
}
