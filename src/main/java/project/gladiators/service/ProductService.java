package project.gladiators.service;

import project.gladiators.service.serviceModels.ProductServiceModel;

import java.util.List;
import java.util.Set;

public interface ProductService {
    void seedProductInDB(ProductServiceModel productServiceModel);

    List<ProductServiceModel> allProducts();

    ProductServiceModel findProductById(String product);

    void editProduct(String id, Set<String> categories, ProductServiceModel model);

    void deleteProduct(String id);

    List<ProductServiceModel> findAllByCategory(String category);
}
