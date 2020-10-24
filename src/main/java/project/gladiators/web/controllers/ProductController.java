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
import project.gladiators.model.bindingModels.ProductEditBindingModel;
import project.gladiators.service.CategoryService;
import project.gladiators.service.CloudinaryService;
import project.gladiators.service.ProductService;
import project.gladiators.service.SubCategoryService;
import project.gladiators.service.serviceModels.CategoryServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.web.viewModels.ProductViewModel;
import project.gladiators.web.viewModels.CategoryViewModel;
import project.gladiators.web.viewModels.SubCategoryViewModel;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController{
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, SubCategoryService subCategoryService, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PageTitle("Add product")
    public ModelAndView addProduct(ModelAndView modelAndView) {
        modelAndView.addObject("product", new ProductEditBindingModel());
        modelAndView.addObject("subCategories", this.subCategoryService.allSubCategories().stream()
                .map(subCategoryServiceModel -> this.modelMapper.map(subCategoryServiceModel, SubCategoryViewModel.class))
                .collect(Collectors.toList()));

        return super.view("/product/product-add",modelAndView);
    }

    @PostMapping("/add")
    public ModelAndView addProduct(@Valid @ModelAttribute(name = "product") ProductEditBindingModel productBindingModel,
                                   BindingResult result, RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("product", productBindingModel);
            modelAndView.addObject("subCategories", this.subCategoryService.allSubCategories().stream()
                    .map(subCategoryServiceModel -> this.modelMapper.map(subCategoryServiceModel, SubCategoryViewModel.class))
                    .collect(Collectors.toList()));
            return super.view("/product/product-add", modelAndView);
        }

        String imageUrl = productBindingModel.getImage().isEmpty() ? "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg"
                : this.cloudinaryService.uploadImageToCurrentFolder(productBindingModel.getImage(), "products");
        ProductServiceModel product = this.modelMapper.map(productBindingModel, ProductServiceModel.class);
        product.setImageUrl(imageUrl);
        this.productService.addProduct(product);

        redirectAttributes.addFlashAttribute("statusMessage", "You created product successful");
        redirectAttributes.addFlashAttribute("statusCode", "successful");
        return super.redirect("/products/all");
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView productDetails(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("product", this.modelMapper.map(this.productService.findProductById(id),
                ProductViewModel.class));
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
        String imageUrl = null;
        if (!model.getImage().getOriginalFilename().equals("")){
              imageUrl = this.cloudinaryService.uploadImageToCurrentFolder(model.getImage(), "products");
        }
        ProductServiceModel productServiceModel = this.modelMapper.map(model, ProductServiceModel.class);
        if (imageUrl != null){
            productServiceModel.setImageUrl(imageUrl);
        }
        this.productService.editProduct(id,productServiceModel);
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
