package project.gladiators.service;

import project.gladiators.service.serviceModels.ProductServiceModel;

import java.util.List;

public interface ProductService {
    void addProduct(ProductServiceModel productServiceModel);

    List<ProductServiceModel> allProducts();

    ProductServiceModel findProductById(String product);

    void editProduct(String id, ProductServiceModel model);

    void deleteProduct(String id);

    List<ProductServiceModel> findAllByCategory(String category);
}
