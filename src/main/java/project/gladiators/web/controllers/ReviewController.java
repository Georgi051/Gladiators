package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.bindingModels.CommentAddBindingModel;
import project.gladiators.service.ProductService;
import project.gladiators.service.ReviewService;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.ReviewServiceModel;
import project.gladiators.web.viewModels.CommentViewModel;
import project.gladiators.web.viewModels.Product;
import project.gladiators.web.viewModels.RatingViewModel;
import project.gladiators.web.viewModels.ReviewViewModel;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReviewController extends BaseController{
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;
    private final ProductService productService;

    @Autowired
    public ReviewController(ReviewService reviewService, ModelMapper modelMapper, ProductService productService) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @PostMapping("/addComment")
    @PageTitle("Product info")
    public ModelAndView addProduct(@ModelAttribute(name = "comment") CommentAddBindingModel commentAddBindingModel ,
                                   Principal principal, String id, ModelAndView modelAndView) {
        ProductServiceModel productById = this.productService.findProductById(id);
        Product product = this.modelMapper.map(productById, Product.class);

        ReviewServiceModel reviewServiceModel = this.reviewService.addReview(commentAddBindingModel.getDescription(),
                        commentAddBindingModel.getStars(), principal.getName(), productById);
        RatingViewModel ratingViewModel = this.modelMapper.map(this.reviewService.productRating(id), RatingViewModel.class);
        List<ReviewViewModel> reviewViewModels = this.reviewService.findAllReviewByProductId(id)
                .stream().map(p -> this.modelMapper.map(p,ReviewViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("product",product);
        modelAndView.addObject("comment",modelMapper.map(reviewServiceModel,CommentViewModel.class));
        modelAndView.addObject("ratingProduct",ratingViewModel);
        modelAndView.addObject("reviews",reviewViewModels);
        return super.view("/product/details",modelAndView);
    }
}
