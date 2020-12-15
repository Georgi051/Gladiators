package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.gladiators.model.entities.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,String> {

    List<Review> findAllByProductId(String id);

    Review findByUserIdAndProductId(String id,String productId);

}
