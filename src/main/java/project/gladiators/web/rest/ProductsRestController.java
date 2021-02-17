package project.gladiators.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gladiators.service.ProductService;
import project.gladiators.web.viewModels.Product;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductsRestController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductsRestController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> allProductsBySearchText(){
        List<Product> products = this.productService.allProducts()
                .stream()
                .filter(product -> !product.isDeleted())
                .filter(product -> !product.getName().equals("Training Plan"))
                .map(p -> this.modelMapper.map(p, Product.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

}
