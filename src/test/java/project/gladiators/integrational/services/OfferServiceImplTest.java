package project.gladiators.integrational.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.model.entities.Offer;
import project.gladiators.model.entities.Product;
import project.gladiators.repository.OfferRepository;
import project.gladiators.service.OfferService;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.OfferServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {
    private final String PRODUCT_NAME = "Syntha-6";
    private final String PRODUCT_CHANGED_NAME = "X-tend";
    private final String PRODUCT_MANUFACTURER_NAME = "BSN";
    private final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(60.0);
    private final BigDecimal OFFER_PRICE = BigDecimal.valueOf(30.0);

    @Autowired
    OfferService offerService;

    @MockBean
    ProductService productService;

    @MockBean
    OfferRepository offerRepository;

    @Autowired
    ModelMapper modelMapper;

    private Offer offer;
    private Product product;
    private OfferServiceModel offerServiceModel;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId("1");
        product.setName(PRODUCT_NAME);
        product.setManufacturerName(PRODUCT_MANUFACTURER_NAME);
        product.setPrice(PRODUCT_PRICE);

        offer = new Offer();
        offer.setId("1");
        offer.setPrice(OFFER_PRICE);
        offer.setProduct(product);

        ProductServiceModel productServiceModelWithChangedName = this.modelMapper.map(product, ProductServiceModel.class);
        productServiceModelWithChangedName.setName(PRODUCT_CHANGED_NAME);

        offerServiceModel = this.modelMapper.map(offer, OfferServiceModel.class);
        offerServiceModel.setId("2");
        offerServiceModel.setProduct(productServiceModelWithChangedName);
    }

    @Test
    void testFindAllOffersShouldReturnCorrectResult() {
        when(this.offerRepository.findAll())
                .thenReturn(List.of(offer));

        List<OfferServiceModel> offerServiceModelList = this.offerService.findAllOffers();
        OfferServiceModel testOffer = offerServiceModelList.get(0);

        Assertions.assertEquals(1, offerServiceModelList.size());
        Assertions.assertEquals(offer.getProduct().getName(), testOffer.getProduct().getName());
        Assertions.assertEquals(offer.getProduct().getManufacturerName(), testOffer.getProduct().getManufacturerName());
    }

    @Test
    void checkIfProductHasOfferShouldReturnCorrectResult() {
        when(this.offerRepository.findAll())
                .thenReturn(List.of(offer));

        ProductServiceModel productServiceModel = this.modelMapper.map(product, ProductServiceModel.class);
        OfferServiceModel offerServiceModel = this.offerService.checkIfProductHasOffer(productServiceModel);

        Assertions.assertEquals(offer.getProduct().getId(), offerServiceModel.getProduct().getId());
        Assertions.assertEquals(offer.getProduct().getName(), offerServiceModel.getProduct().getName());
        Assertions.assertEquals(offer.getProduct().getManufacturerName(), offerServiceModel.getProduct().getManufacturerName());
        Assertions.assertEquals(offer.getProduct().getManufacturerName(), offerServiceModel.getProduct().getManufacturerName());
    }

    @Test
    void checkIfTheOfferHasNoProductShouldReturnNullValue() {
        when(this.offerRepository.findAll())
                .thenReturn(List.of(modelMapper.map(offerServiceModel, Offer.class)));

        ProductServiceModel productServiceModel = this.modelMapper.map(product, ProductServiceModel.class);
        OfferServiceModel testOfferServiceModel = this.offerService.checkIfProductHasOffer(productServiceModel);

        assertNull(testOfferServiceModel.getProduct());
    }

    @Test
    void testGetProductOfferPriceShouldReturnCorrectResult() {
        when(this.offerRepository.findAll())
                .thenReturn(List.of(offer));

        ProductServiceModel productServiceModel = this.modelMapper.map(product, ProductServiceModel.class);
        BigDecimal testPrice = this.offerService.getProductOfferPrice(productServiceModel);
        Assertions.assertEquals(testPrice, OFFER_PRICE);
    }

    @Test
    void testGetProductOfferPriceShouldReturnNullValue() {
        when(this.offerRepository.findAll())
                .thenReturn(List.of(modelMapper.map(offerServiceModel, Offer.class)));

        ProductServiceModel productServiceModel = this.modelMapper.map(product, ProductServiceModel.class);
        BigDecimal testPrice = this.offerService.getProductOfferPrice(productServiceModel);
        assertNull(testPrice);
    }

    @Test
    void testFindByProductIdShouldReturnCorrectResult() {
        when(this.offerRepository.findByProduct_Id("1"))
                .thenReturn(Optional.ofNullable(offer));

        OfferServiceModel testOffer = this.offerService.findByProductId("1");

        Assertions.assertEquals(offer.getId(), testOffer.getId());
        Assertions.assertEquals(offer.getProduct().getName(), testOffer.getProduct().getName());
    }

    @Test
    void testFindByProductIdShouldReturnNullValue() {
        when(this.offerRepository.findByProduct_Id("1"))
                .thenReturn(Optional.ofNullable(offer));

        OfferServiceModel testOffer = this.offerService.findByProductId("2");
        assertNull(testOffer);
    }

}