package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.exceptions.ProductNotFoundException;
import project.gladiators.model.entities.Product;
import project.gladiators.repository.ProductRepository;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.ProductServiceModel;

import java.util.List;
import java.util.stream.Collectors;

import static project.gladiators.constants.ExceptionMessages.PRODUCT_NOT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
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
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Override
    public void editProduct(String id, ProductServiceModel model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
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
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        this.productRepository.delete(product);
    }

    @Override
    public List<ProductServiceModel> findAllByCategory(String category) {
        return this.productRepository.findAll().stream()
                .filter(p -> p.getSubCategory().getProducts().stream().anyMatch(c -> c.getName().equals(category)))
                .map(p -> this.modelMapper.map(p,ProductServiceModel.class))
                .collect(Collectors.toList());
    }
}
