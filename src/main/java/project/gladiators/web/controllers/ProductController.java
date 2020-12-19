package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.bindingModels.CommentAddBindingModel;
import project.gladiators.model.bindingModels.ProductAddBindingModel;
import project.gladiators.model.bindingModels.ProductEditBindingModel;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.OfferServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.RatingServiceModel;
import project.gladiators.service.serviceModels.ReviewServiceModel;
import project.gladiators.web.viewModels.ProductViewModel;
import project.gladiators.web.viewModels.RatingViewModel;
import project.gladiators.web.viewModels.ReviewViewModel;
import project.gladiators.web.viewModels.SubCategoryViewModel;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController{
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final ReviewService reviewService;
    private final OfferService offerService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService,
                             SubCategoryService subCategoryService, CloudinaryService cloudinaryService,
                             ModelMapper modelMapper, ReviewService reviewService, OfferService offerService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.reviewService = reviewService;
        this.offerService = offerService;
    }

    @GetMapping("/add")
    @PageTitle("Add product")
    public ModelAndView addProduct(ModelAndView modelAndView) {
        modelAndView.addObject("product", new ProductAddBindingModel());
        modelAndView.addObject("subCategories", this.subCategoryService.allSubCategories().stream()
                .map(subCategoryServiceModel -> this.modelMapper.map(subCategoryServiceModel, SubCategoryViewModel.class))
                .collect(Collectors.toList()));

        return super.view("/product/product-add",modelAndView);
    }

    @PostMapping("/add")
    public ModelAndView addProduct(@Valid @ModelAttribute(name = "product") ProductAddBindingModel productBindingModel,
                                   BindingResult result, RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("product", productBindingModel);
            modelAndView.addObject("subCategories", this.subCategoryService.allSubCategories().stream()
                    .map(subCategoryServiceModel -> this.modelMapper.map(subCategoryServiceModel, SubCategoryViewModel.class))
                    .collect(Collectors.toList()));
            return super.view("/product/product-add", modelAndView);
        }
        ProductServiceModel product = this.modelMapper.map(productBindingModel, ProductServiceModel.class);
        this.productService.addProduct(product,productBindingModel.getImage());
        redirectAttributes.addFlashAttribute("statusMessage", "You created product successful");
        redirectAttributes.addFlashAttribute("statusCode", "successful");
        return super.redirect("/products/all");
    }

    @GetMapping("/details/{id}")
    @PageTitle("Product info")
    public ModelAndView productDetails(@PathVariable String id, ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService
                .findProductById(id);
        modelAndView.addObject("product", this.modelMapper
                .map(productServiceModel,
                ProductViewModel.class));

        RatingServiceModel ratingServiceModel = this.reviewService.RatingServiceModel(id);
        RatingViewModel ratingViewModel = this.modelMapper.map(ratingServiceModel, RatingViewModel.class);
        List<ReviewViewModel> reviewViewModels = this.reviewService.findAllReviewByProductId(id)
                .stream().map(p -> this.modelMapper.map(p,ReviewViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("ratingProduct",ratingViewModel);
        modelAndView.addObject("comment",new CommentAddBindingModel());
        modelAndView.addObject("reviews",reviewViewModels);
        modelAndView.addObject("offerPrice", this.offerService.getProductOfferPrice(productServiceModel));
        return super.view("/product/details", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ModelAndView editProduct(@PathVariable String id, ModelAndView modelAndView) {
        ProductViewModel product = mapProductDetails(id);
        modelAndView.addObject("product", product);
        modelAndView.addObject("productId", id);
        return super.view("product/edit-product", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ModelAndView confirmEditProduct(@PathVariable String id,@Valid @ModelAttribute ProductEditBindingModel model) throws IOException {
        ProductServiceModel productServiceModel = this.modelMapper.map(model, ProductServiceModel.class);
        this.productService.editProduct(id,productServiceModel,model.getImage());
        return super.redirect("/products/all");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ModelAndView deleteProduct(@PathVariable String id, ModelAndView modelAndView){
        ProductViewModel product = mapProductDetails(id);
        modelAndView.addObject("product", product);
        modelAndView.addObject("productId", id);
        return super.view("/product/delete-product", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ModelAndView confirmDeleteProduct(@PathVariable String id) {
        this.productService.deleteProduct(id);
        return super.redirect("/products/all");
    }

    @GetMapping("/all")
    public ModelAndView allProduct(ModelAndView modelAndView) {
        modelAndView.addObject("products", this.productService.allProducts().stream()
                .map(p -> this.modelMapper.map(p, ProductViewModel.class)).collect(Collectors.toList()));
        return super.view("/product/all-products", modelAndView);
    }



    private ProductViewModel mapProductDetails(String id) {
        return this.modelMapper.map(this.productService.findProductById(id), ProductViewModel.class);
    }

}
