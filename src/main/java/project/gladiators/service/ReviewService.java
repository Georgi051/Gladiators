package project.gladiators.service;

import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.RatingServiceModel;
import project.gladiators.service.serviceModels.ReviewServiceModel;

import java.util.List;

public interface ReviewService {

    ReviewServiceModel addReview(String description, int stars, String userName, ProductServiceModel productById);

    List<ReviewServiceModel> findAllReviewByProductId(String id);

    RatingServiceModel productRating(String id);

    void addReviewToCurrentProduct(ProductServiceModel product, ReviewServiceModel review);
}
