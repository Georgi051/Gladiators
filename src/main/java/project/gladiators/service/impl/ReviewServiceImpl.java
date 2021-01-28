package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.Product;
import project.gladiators.model.entities.Review;
import project.gladiators.model.entities.User;
import project.gladiators.repository.ReviewRepository;
import project.gladiators.service.ProductService;
import project.gladiators.service.ReviewService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.RatingServiceModel;
import project.gladiators.service.serviceModels.ReviewServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static project.gladiators.constants.UserMassages.ALREADY_COMMENT;
import static project.gladiators.constants.UserMassages.NEW_COMMENT;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductService productService, UserService userService, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @Override
    public ReviewServiceModel addReview(String description, int stars, String userName, ProductServiceModel productServiceModel) {
        UserServiceModel client = userService.findUserByUsername(userName);
        Product product = modelMapper.map(productServiceModel, Product.class);
        ReviewServiceModel reviewServiceModel = new ReviewServiceModel();
        if (reviewRepository.findByUserIdAndProductId(client.getId(),product.getId()) == null &&
        !description.equals("")){
            Review review = new Review();
            review.setDescription(description);
            review.setReviewDate(LocalDateTime.now());
            review.setUser(this.modelMapper.map(client, User.class));
            review.setProduct(product);
            review.setStars(stars);
            productService.addReviewToCurrentProduct(productServiceModel,this.modelMapper.map(review,ReviewServiceModel.class)
            );
            this.reviewRepository.save(review);
            reviewServiceModel.setDescription(NEW_COMMENT);
        }else {
             reviewServiceModel.setDescription(ALREADY_COMMENT);
        }
        return reviewServiceModel;
    }

    @Override
    public List<ReviewServiceModel> findAllReviewByProductId(String id){
        return this.reviewRepository.findAllByProductId(id)
                .stream()
                .map(r -> this.modelMapper.map(r,ReviewServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public RatingServiceModel RatingServiceModel(String id) {
        List<Integer> starsList = new ArrayList<>();

        reviewRepository.findAllByProductId(id).forEach(r -> {
            starsList.add(r.getStars());
        });
        double ratingCounting = ratingCounting(starsList);

        RatingServiceModel ratingServiceModel = new RatingServiceModel();

        if (ratingCounting > 0 && starsList.size() > 0){
            ratingServiceModel.setRating(ratingCounting);
            ratingServiceModel.setVotes(starsList.size());
        }else {
            ratingServiceModel.setRating(0);
            ratingServiceModel.setVotes(0);
        }

        return ratingServiceModel;
    }

    private double ratingCounting(List<Integer> stars){
        int oneStar = 0;
        int twoStar = 0;
        int threeStar = 0;
        int fourStar = 0;
        int fiveStar = 0;
        for (Integer star : stars) {
            if (star == 1){
                oneStar++;
            }else if (star == 2){
                twoStar++;
            }else if (star == 3){
                threeStar++;
            }else if (star == 4){
                fourStar++;
            }else if (star == 5){
                fiveStar++;
            }
        }

        return (5.0 * fiveStar + 4.0 * fourStar + 3.0 * threeStar + 2.0 * twoStar + oneStar) / stars.size();
    }

}
