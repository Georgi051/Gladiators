package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.exceptions.ProductNotFoundException;
import project.gladiators.model.entities.Category;
import project.gladiators.model.entities.Product;
import project.gladiators.repository.ProductRepository;
import project.gladiators.service.CategoryService;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.ProductServiceModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void addProduct(ProductServiceModel productServiceModel) {
        if (this.productRepository.findByName(productServiceModel.getName()) == null) {
            Product product = this.modelMapper.map(productServiceModel, Product.class);
            product.setBuyingCounter(0);
            product.setReviews(null);
            this.productRepository.saveAndFlush(product);
        }
    }

    @Override
    public List<ProductServiceModel> allProducts() {
        return this.productRepository.findAll().stream()
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductServiceModel findProductById(String id) {
        return this.productRepository.findById(id)
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with ID %s not found", id)));
    }

    @Override
    public void editProduct(String id, ProductServiceModel model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with ID %s not found", id)));
        product.setName(model.getName());
        product.setPrice(model.getPrice());
        product.setDescription(model.getDescription());
        product.setQuantity(model.getQuantity());
        if (model.getImageUrl() != null){
            product.setImageUrl(model.getImageUrl());
        }
        this.modelMapper.map(this.productRepository.saveAndFlush(product), ProductServiceModel.class);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with ID %s not found", id)));
        this.productRepository.delete(product);
    }

    @Override
    public List<ProductServiceModel> findAllByCategory(String category) {
        return this.productRepository.findAll().stream()
                .filter(p -> p.getCategories().stream().anyMatch(c -> c.getName().equals(category)))
                .map(p -> this.modelMapper.map(p,ProductServiceModel.class))
                .collect(Collectors.toList());
    }
}
