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
import project.gladiators.model.entities.Product;
import project.gladiators.model.entities.Review;
import project.gladiators.model.entities.User;
import project.gladiators.repository.ProductRepository;
import project.gladiators.repository.ReviewRepository;
import project.gladiators.repository.UserRepository;
import project.gladiators.service.ProductService;
import project.gladiators.service.ReviewService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.RatingServiceModel;
import project.gladiators.service.serviceModels.ReviewServiceModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static project.gladiators.constants.UserMessages.ALREADY_COMMENT;
import static project.gladiators.constants.UserMessages.NEW_COMMENT;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
    private final String USER_USERNAME = "testUserName";
    private final String REVIEW_DESCRIPTION = "Best product";

    @Autowired
    ReviewService reviewService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    ReviewRepository reviewRepository;

    @MockBean
    UserRepository userRepository;

    private User user;
    private Review review;
    private ProductServiceModel productServiceModel;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername(USER_USERNAME);
        user.setId("1");
        user.setEmail("testEmail");

        productServiceModel = new ProductServiceModel();
        productServiceModel.setId("1");
        productServiceModel.setManufacturerName("BSN");
        productServiceModel.setName("Syntha-6");
        productServiceModel.setQuantity(4);
        productServiceModel.setReviews(new ArrayList<>());

        review = new Review();
        review.setId("1");
        review.setDescription(REVIEW_DESCRIPTION);
        review.setReviewDate(LocalDateTime.now());
        review.setUser(user);
        review.setProduct(this.modelMapper.map(productServiceModel, Product.class));
        review.setStars(4);
    }

    @Test
    void testAddReviewShouldReturnCorrectResult() {
        when(this.userRepository.saveAndFlush(Mockito.any(User.class)))
                .thenReturn(user);

        when(this.userRepository.findUserByUsername(USER_USERNAME))
                .thenReturn(Optional.ofNullable(user));

        when(this.productRepository.save(any(Product.class)))
                .thenReturn(this.modelMapper.map(productServiceModel, Product.class));

        ReviewServiceModel reviewServiceModel = this.reviewService.addReview(REVIEW_DESCRIPTION, 4, user.getUsername(), productServiceModel);

        Assertions.assertEquals(review.getUser().getUsername(), reviewServiceModel.getUser().getUsername());
        Assertions.assertEquals(review.getStars(), reviewServiceModel.getStars());
        Assertions.assertEquals(NEW_COMMENT, reviewServiceModel.getDescription());
    }

    @Test
    void testAddReviewShouldReturnAlreadyCommentResultWhenDescriptionIsEmptyString() {
        when(this.userRepository.saveAndFlush(Mockito.any(User.class)))
                .thenReturn(user);

        when(this.userRepository.findUserByUsername(USER_USERNAME))
                .thenReturn(Optional.ofNullable(user));

        when(this.productRepository.save(any(Product.class)))
                .thenReturn(this.modelMapper.map(productServiceModel, Product.class));

        ReviewServiceModel reviewServiceModel = this.reviewService.addReview("", 4, user.getUsername(), productServiceModel);

        Assertions.assertEquals(ALREADY_COMMENT, reviewServiceModel.getDescription());
    }


    @Test
    void testAddReviewShouldReturnAlreadyCommentResultWhenUserIdAndProductIdExist() {
        when(this.userRepository.saveAndFlush(Mockito.any(User.class)))
                .thenReturn(user);

        when(this.userRepository.findUserByUsername(USER_USERNAME))
                .thenReturn(Optional.ofNullable(user));

        when(this.reviewRepository.findByUserIdAndProductId(user.getId(), productServiceModel.getId()))
                .thenReturn(review);

        when(this.productRepository.save(any(Product.class)))
                .thenReturn(this.modelMapper.map(productServiceModel, Product.class));

        ReviewServiceModel reviewServiceModel = this.reviewService.addReview(REVIEW_DESCRIPTION, 4, user.getUsername(), productServiceModel);

        Assertions.assertEquals(ALREADY_COMMENT, reviewServiceModel.getDescription());
    }

        @Test
        public void addReviewToCurrentProduct_shouldAddReviews(){
        Product product = modelMapper.map(productServiceModel, Product.class);
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));
        Review review = new Review();
        review.setId("1");
        review.setStars(5);
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(review);
        reviewService.addReviewToCurrentProduct(this.modelMapper
        .map(product, ProductServiceModel.class), modelMapper.map(review, ReviewServiceModel.class));

        when(productRepository.save(any()))
                .thenReturn(product);

    }

    @Test
    void testFindAllReviewByProductIdShouldReturnCorrectResult() {
        when(this.reviewRepository.findAllByProductId(productServiceModel.getId()))
                .thenReturn(List.of(review));

        List<ReviewServiceModel> reviewServiceModelList = this.reviewService.findAllReviewByProductId(productServiceModel.getId());
        ReviewServiceModel testReview = reviewServiceModelList.get(0);

        Assertions.assertEquals(1, reviewServiceModelList.size());
        Assertions.assertEquals(review.getUser().getUsername(), testReview.getUser().getUsername());
        Assertions.assertEquals(review.getDescription(), testReview.getDescription());
    }

    @Test
    void testProductRatingWithCommentShouldReturnCorrectResult() {
        when(this.reviewRepository.findAllByProductId(productServiceModel.getId()))
                .thenReturn(List.of(review));
        List<ReviewServiceModel> reviewServiceModelList = this.reviewService.findAllReviewByProductId(productServiceModel.getId());

        RatingServiceModel ratingServiceModel = this.reviewService.productRating(review.getId());

        Assertions.assertEquals(1, reviewServiceModelList.size());
        Assertions.assertEquals(Double.valueOf(review.getStars()), ratingServiceModel.getRating());
        Assertions.assertEquals(1, ratingServiceModel.getVotes());
    }

    @Test
    void testProductRatingWithNoCommentShouldReturnCorrectResult() {
        when(this.reviewRepository.findAllByProductId(productServiceModel.getId()))
                .thenReturn(new ArrayList<>());

        List<ReviewServiceModel> reviewServiceModelList = this.reviewService.findAllReviewByProductId(productServiceModel.getId());
        RatingServiceModel ratingServiceModel = this.reviewService.productRating(review.getId());

        Assertions.assertEquals(0, reviewServiceModelList.size());
        Assertions.assertEquals(0.0, ratingServiceModel.getRating());
        Assertions.assertEquals(0, ratingServiceModel.getVotes());
    }

}