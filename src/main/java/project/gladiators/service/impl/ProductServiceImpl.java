package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.repository.ProductRepository;
import project.gladiators.service.CategoryService;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.ProductServiceModel;

import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedProductInDB(ProductServiceModel productServiceModel) {

    }

    @Override
    public List<ProductServiceModel> allProducts() {
        return null;
    }

    @Override
    public ProductServiceModel findProductById(String product) {
        return null;
    }

    @Override
    public void editProduct(String id, Set<String> categories, ProductServiceModel model) {

    }

    @Override
    public void deleteProduct(String id) {

    }

    @Override
    public List<ProductServiceModel> findAllByCategory(String category) {
        return null;
    }
}
