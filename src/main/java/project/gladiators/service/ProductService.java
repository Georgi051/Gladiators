package project.gladiators.service;

import org.springframework.web.multipart.MultipartFile;
import project.gladiators.service.serviceModels.ProductServiceModel;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    void addProduct(ProductServiceModel productServiceModel, MultipartFile image) throws IOException;

    List<ProductServiceModel> allProducts();

    ProductServiceModel findProductById(String product);

    void editProduct(String id, ProductServiceModel model, MultipartFile image) throws IOException;

    void deleteProduct(String id);
//
//    List<ProductServiceModel> findAllByCategory(String category);
}
