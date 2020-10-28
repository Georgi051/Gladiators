package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.exceptions.ProductNotFoundException;
import project.gladiators.model.entities.Product;
import project.gladiators.repository.ProductRepository;
import project.gladiators.service.CloudinaryService;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.ProductServiceModel;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static project.gladiators.constants.ExceptionMessages.PRODUCT_NOT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addProduct(ProductServiceModel productServiceModel, MultipartFile productImage) throws IOException {
        if (this.productRepository.findByName(productServiceModel.getName()) == null) {
            Product product = this.modelMapper.map(productServiceModel, Product.class);
            String imageUrl = productImage.isEmpty() ? "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg"
                    : this.cloudinaryService.uploadImageToCurrentFolder(productImage, "products");
            product.setBuyingCounter(0);
            product.setReviews(null);
            product.setImageUrl(imageUrl);
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
    public void editProduct(String id, ProductServiceModel model, MultipartFile productImage) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        String imageUrl = null;
        if (!productImage.getOriginalFilename().equals("")){
            imageUrl = this.cloudinaryService.uploadImageToCurrentFolder(productImage, "products");
        }
        if (imageUrl != null){
            product.setImageUrl(imageUrl);
        }
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
