package project.gladiators.integrational;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.entities.Offer;
import project.gladiators.model.entities.Product;
import project.gladiators.model.entities.Review;
import project.gladiators.model.entities.SubCategory;
import project.gladiators.repository.OfferRepository;
import project.gladiators.repository.ProductRepository;
import project.gladiators.repository.ReviewRepository;
import project.gladiators.repository.SubCategoryRepository;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.OrderProductServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.ReviewServiceModel;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    Product product;
    SubCategory subCategory;
    Offer offer;
    MultipartFile multipartFile;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    SubCategoryRepository subCategoryRepository;

    @MockBean
    OfferRepository offerRepository;

    @MockBean
    ReviewRepository reviewRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        product = new Product();
        product.setId("1");
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(5));
        product.setDeleted(false);
        product.setQuantity(20);
        product.setBuyingCounter(0);
        product.setReviews(new ArrayList<>());
        multipartFile =  new MockMultipartFile("picture",
                "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg",
                IMAGE_JPEG.getMimeType(), (byte[]) null);
        subCategory = new SubCategory();
        subCategory.setEmpty(true);
        subCategory.setId("1");
        subCategory.setName("Test Subcategory");
        offer = new Offer();
        offer.setProduct(product);
        offer.setPrice(BigDecimal.valueOf(4));

    }

    @Test
    public void addProduct_shouldCreateProduct() throws IOException {

        when(subCategoryRepository.findById("1"))
                .thenReturn(Optional.of(subCategory));
        when(offerRepository.findByProduct_Id("1"))
                .thenReturn(Optional.of(offer));

        ProductServiceModel productServiceModel = modelMapper.map(product, ProductServiceModel.class);

        productServiceModel.setSubCategory(modelMapper.map(subCategory, SubCategoryServiceModel.class));
        productService.addProduct(productServiceModel, multipartFile);

        verify(productRepository).saveAndFlush(any());

    }

    @Test
    public void addProduct_shouldChangeCategoryEmptyStatusIfTheProductInSubcategoryIsFirst() throws IOException {

        when(subCategoryRepository.findById("1"))
                .thenReturn(Optional.of(subCategory));
        when(offerRepository.findByProduct_Id("1"))
                .thenReturn(Optional.of(offer));

        ProductServiceModel productServiceModel = modelMapper.map(product, ProductServiceModel.class);
        productServiceModel.setSubCategory(modelMapper.map(subCategory, SubCategoryServiceModel.class));
        productService.addProduct(productServiceModel, multipartFile);

        assertFalse(subCategory.isEmpty());
    }

    @Test
    public void findProductById_shouldReturnCorrectProduct(){
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        ProductServiceModel productServiceModel = productService.findProductById("1");

        assertEquals(product.getName(), productServiceModel.getName());
    }

    @Test
    public void editProduct_shouldChangeParamsOfTheProduct() throws IOException {

        ProductServiceModel productServiceModel = modelMapper.map(product, ProductServiceModel.class);
        productServiceModel.setPrice(BigDecimal.valueOf(7));
        productServiceModel.setQuantity(30);
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        productService.editProduct("1", productServiceModel, multipartFile);

        Product testProduct = productRepository.findById("1").orElse(null);
        assertEquals(30, testProduct.getQuantity());

    }

    @Test
    public void editProduct_shouldChangeHisPictureIfYouAddNewOne() throws IOException {
        ProductServiceModel productServiceModel = modelMapper.map(product, ProductServiceModel.class);
        productServiceModel.setQuantity(30);
        productServiceModel.setImageUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.metoffice.gov.uk%2Fresearch%2Fweather%2Fsatellite-applications%2Fsatellite-image-of-the-month&psig=AOvVaw2rv--Jr-GCeXG3DAWJ5vE3&ust=1611931455925000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCIjcreLuvu4CFQAAAAAdAAAAABAD");
        productServiceModel.setPrice(BigDecimal.valueOf(10));
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        productService.editProduct("1", productServiceModel, multipartFile);

        Product testProduct = productRepository.findById("1").orElse(null);
        assertEquals(productServiceModel.getImageUrl(), testProduct.getImageUrl());
    }

    @Test
    public void editProduct_ifProductHasOfferShouldChangeOfferPrice() throws IOException {
        ProductServiceModel productServiceModel = modelMapper.map(product, ProductServiceModel.class);
        productServiceModel.setPrice(BigDecimal.valueOf(10));
        productServiceModel.setQuantity(30);
        productServiceModel.setImageUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.metoffice.gov.uk%2Fresearch%2Fweather%2Fsatellite-applications%2Fsatellite-image-of-the-month&psig=AOvVaw2rv--Jr-GCeXG3DAWJ5vE3&ust=1611931455925000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCIjcreLuvu4CFQAAAAAdAAAAABAD");
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));
        when(offerRepository.findByProduct_Id(product.getId()))
                .thenReturn(Optional.of(offer));
        productService.editProduct("1", productServiceModel, multipartFile);


        when(offerRepository.save(any())).thenReturn(offer);
        assertEquals(8, offer.getPrice().intValue());
    }


    @Test
    public void editProduct_ifProductIsInOfferShouldChangeOfferPrice(){
        ProductServiceModel productServiceModel = modelMapper.map(product, ProductServiceModel.class);
        productServiceModel.setPrice(BigDecimal.valueOf(7));
        productServiceModel.setQuantity(30);
        productServiceModel.setImageUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.metoffice.gov.uk%2Fresearch%2Fweather%2Fsatellite-applications%2Fsatellite-image-of-the-month&psig=AOvVaw2rv--Jr-GCeXG3DAWJ5vE3&ust=1611931455925000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCIjcreLuvu4CFQAAAAAdAAAAABAD");

    }

    @Test
    public void deleteProduct_shouldDeleteProductByGivenId(){

        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));
        subCategory.getProducts().add(product);
        when(subCategoryRepository.findByProducts(product))
                .thenReturn(subCategory);
        productService.deleteProduct("1");

        Product testProduct = productRepository.findById("1").orElse(null);

        assertTrue(testProduct.isDeleted());
    }

    @Test
    public void deleteProduct_shouldRestoreProductByGivenId(){

        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));
        subCategory.getProducts().add(product);
        when(subCategoryRepository.findByProducts(product))
                .thenReturn(subCategory);

        productService.restoreProduct("1");

        Product testProduct = productRepository.findById("1").orElse(null);

        assertFalse(testProduct.isDeleted());
    }

    @Test
    public void addReviewToCurrentProduct_shouldAddReviews(){

        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));
        Review review = new Review();
        review.setId("1");
        review.setStars(5);
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(review);
        productService.addReviewToCurrentProduct(this.modelMapper
        .map(product, ProductServiceModel.class), modelMapper.map(review, ReviewServiceModel.class));

        when(productRepository.save(any()))
                .thenReturn(product);

    }

    @Test
    public void findByName_shouldReturnCorrectProduct(){
        when(productRepository.findByName("Test Product"))
                .thenReturn(product);

        ProductServiceModel productServiceModel = productService.findByName(product.getName());

        assertEquals("1", productServiceModel.getId());
    }

    @Test
    public void sellProduct_shouldDecreaseProductQuantity(){
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        OrderProductServiceModel orderProductServiceModel = new OrderProductServiceModel();
        orderProductServiceModel.setProduct(modelMapper.map(product, ProductServiceModel.class));
        orderProductServiceModel.getProduct().setBuyingProductsQuantity(3);
        orderProductServiceModel.setQuantity(3);
        orderProductServiceModel.setId("1");
        List<OrderProductServiceModel> products = new ArrayList<>();
        products.add(orderProductServiceModel);
        productService.sellProduct(products);

        assertEquals(17, product.getQuantity());
    }

    @Test
    public void sellProduct_ifProductQuantityIsSmallerThanBuyingQuantityMustSetZeroProductQuantity(){
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        OrderProductServiceModel orderProductServiceModel = new OrderProductServiceModel();
        orderProductServiceModel.setProduct(modelMapper.map(product, ProductServiceModel.class));
        orderProductServiceModel.getProduct().setBuyingProductsQuantity(21);
        orderProductServiceModel.setQuantity(17);
        orderProductServiceModel.setId("1");
        List<OrderProductServiceModel> products = new ArrayList<>();
        products.add(orderProductServiceModel);
        productService.sellProduct(products);

        assertEquals(0, product.getQuantity());
    }

}
