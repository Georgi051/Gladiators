package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.exceptions.ProductDeleteException;
import project.gladiators.exceptions.ProductNotFoundException;
import project.gladiators.model.entities.Category;
import project.gladiators.model.entities.Product;
import project.gladiators.model.entities.SubCategory;
import project.gladiators.repository.OfferRepository;
import project.gladiators.repository.ProductRepository;
import project.gladiators.repository.SubCategoryRepository;
import project.gladiators.service.CloudinaryService;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.OrderProductServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static project.gladiators.constants.ExceptionMessages.PRODUCT_NOT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final OfferRepository offerRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, SubCategoryRepository subCategoryRepository, OfferRepository offerRepository, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.offerRepository = offerRepository;
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
            product.setDeleted(false);
            SubCategoryServiceModel subCategoryServiceModel = productServiceModel
                    .getSubCategory();
            SubCategory subCategory = this.subCategoryRepository
                    .findById(subCategoryServiceModel.getId()).orElse(null);
            if(subCategory.getProducts().isEmpty()){
                subCategory.setEmpty(false);
            }
            subCategory.getProducts().add(product);
            this.productRepository.saveAndFlush(product);
            this.offerRepository.findByProduct_Id(product.getId())
                    .ifPresent((o) -> { o.setPrice(product.getPrice().multiply(new BigDecimal(0.8)));
                        this.offerRepository.save(o);
                    });
            this.subCategoryRepository.saveAndFlush(subCategory);
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
        if (!productImage.getOriginalFilename().equals("")) {
            imageUrl = this.cloudinaryService.uploadImageToCurrentFolder(productImage, "products");
        }
        if (imageUrl != null) {
            product.setImageUrl(imageUrl);
        }
        product.setName(model.getName());
        product.setPrice(model.getPrice());
        product.setDescription(model.getDescription());
        product.setQuantity(model.getQuantity());
        if (model.getImageUrl() != null) {
            product.setImageUrl(model.getImageUrl());
        }

        this.offerRepository.findByProduct_Id(product.getId())
                .ifPresent((o) -> { o.setPrice(product.getPrice().multiply(new BigDecimal(0.8)));
                    this.offerRepository.save(o);
                });

        this.modelMapper.map(this.productRepository.saveAndFlush(product), ProductServiceModel.class);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        product.setDeleted(true);
        SubCategory subCategory = this.subCategoryRepository.findByProducts(product);
        List<Product> products = subCategory.getProducts().stream()
                .filter(product1 -> !product1.isDeleted()).collect(Collectors.toList());
        if(products.isEmpty()){
            subCategory.setEmpty(true);
            this.subCategoryRepository.save(subCategory);
        }
        this.productRepository.save(product);
    }

    @Override
    public void restoreProduct(String id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        SubCategory subCategory = this.subCategoryRepository.findByProducts(product);
        product.setDeleted(false);
        subCategory.setEmpty(false);
        this.productRepository.save(product);

    }

    @Override
    public void sellProduct(List<OrderProductServiceModel> products) {
        List<Product> productsAfterQuantityChanges = products.stream().map(p -> {
            Product product = this.productRepository.findById(p.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

            if (product.getQuantity() < p.getProduct().getBuyingProductsQuantity()) {
                product.setQuantity(0);
            } else {
                product.setQuantity(product.getQuantity() - p.getProduct().getBuyingProductsQuantity());
            }
            product.setBuyingCounter(product.getBuyingCounter() + p.getProduct().getBuyingProductsQuantity());
            return product;
        }).collect(Collectors.toList());

        this.productRepository.saveAll(productsAfterQuantityChanges);
    }


}
