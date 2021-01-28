package project.gladiators.integrational;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.exceptions.MaxProductQuantityInCartException;
import project.gladiators.model.entities.*;
import project.gladiators.model.enums.TrainingPlanType;
import project.gladiators.repository.CustomerRepository;
import project.gladiators.repository.OfferRepository;
import project.gladiators.repository.ProductRepository;
import project.gladiators.repository.TrainingPlanRepository;
import project.gladiators.service.CartService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.OrderServiceModel;
import project.gladiators.web.viewModels.ShoppingCartViewModel;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    Product product;
    Customer customer;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    TrainingPlanRepository trainingPlanRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    OfferRepository offerRepository;

    @Autowired
    CartService cartService;

    @Autowired
    HttpSession httpSession;

    @Autowired
    ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        product = new Product();
        product.setId("1");
        product.setName("Product");
        product.setQuantity(20);
        product.setDescription("Test");
        product.setDeleted(false);
        product.setPrice(BigDecimal.valueOf(5));


        customer = new Customer();
        customer.setId("1");

    }

    @Test
    public void retrieveCart_shouldReturnCart(){
        List<ShoppingCartViewModel> cart = cartService.retrieveCart(httpSession);

        assertTrue(cart != null);
    }

    @Test
    public void addItemToCart_shouldAddItemsToCart(){

        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));
        cartService.retrieveCart(httpSession);
        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);
        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));

        assertEquals(1, shoppingCartViewModel.size());

    }

    @Test
    public void addItemToCart_shouldThrowExceptionIfYouOrderMoreQuantityThanTheShopHas(){

        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));
        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);

        assertThrows(MaxProductQuantityInCartException.class, () ->
                cartService.addItemToCart("1", 22, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class)));

    }

    @Test
    public void addItemToCart_shouldThrowExceptionIfYouOrderMoreThanFifteenQuantity(){

        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));
        cartService.retrieveCart(httpSession);
        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);

        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));
        assertThrows(MaxProductQuantityInCartException.class, () ->
                cartService.addItemToCart("1", 16, shoppingCartViewModel,
                        modelMapper.map(customer, CustomerServiceModel.class)));

    }

    @Test
    public void addItemToCart_shouldSubstringDescriptionOfProduct(){

        product.setDescription("Very Long Description Very Long Description Very Long Description Very Long Description");
        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        cartService.retrieveCart(httpSession);

        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);
        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));

        assertEquals(22, shoppingCartViewModel.get(0).getProduct().getProduct().getDescription().length());
    }

    @Test
    public void addItemToCart_ifTheProductIsAlreadyInTheCartShouldSumItsQuantity(){

        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));
        cartService.retrieveCart(httpSession);
        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);

        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));
        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));

        assertEquals(2, shoppingCartViewModel.get(0).getQuantity());
    }

    @Test
    public void addItemToCart_shouldSetTrainingPlanDescriptionIfNameIsTrainingPlan(){
        product.setName("Training plan");

        TrainingPlan trainingPlan = new TrainingPlan();
        trainingPlan.setCustomers(new HashSet<>());
        trainingPlan.getCustomers().add(customer);
        trainingPlan.setId("1");
        trainingPlan.setTrainingPlanType(TrainingPlanType.LOST_WEIGHT);

        when(trainingPlanRepository.getByCustomers(customer))
                .thenReturn(trainingPlan);
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));
        when(customerRepository.findById("1"))
                .thenReturn(Optional.of(customer));
        cartService.retrieveCart(httpSession);

        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);
        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));

        assertEquals("LOST_WEIGHT", shoppingCartViewModel.get(0)
        .getProduct().getProduct().getDescription());

    }

    @Test
    public void addItemToCart_shouldNotAddTwoTimesTrainingPlan(){
        product.setName("Training plan");

        TrainingPlan trainingPlan = new TrainingPlan();
        trainingPlan.setCustomers(new HashSet<>());
        trainingPlan.getCustomers().add(customer);
        trainingPlan.setId("1");
        trainingPlan.setTrainingPlanType(TrainingPlanType.LOST_WEIGHT);

        when(trainingPlanRepository.getByCustomers(customer))
                .thenReturn(trainingPlan);

        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        when(customerRepository.findById("1"))
                .thenReturn(Optional.of(customer));

        cartService.retrieveCart(httpSession);

        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);

        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));

        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));

        assertEquals(1, shoppingCartViewModel.size());

    }

    @Test
    public void addItemToCart_ifProductHasOfferShouldChangeHisPrice(){

        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);

        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        Offer offer = new Offer();
        offer.setProduct(product);
        offer.setPrice(BigDecimal.valueOf(4));
        offer.setId("1");

        when(offerRepository.findByProduct_Id("1"))
                .thenReturn(Optional.of(offer));

        cartService.retrieveCart(httpSession);

        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);

        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));

        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));


        assertEquals(BigDecimal.valueOf(4), shoppingCartViewModel.get(0)
        .getProduct().getPrice());
    }

    @Test
    public void removeItemFromCart_shouldRemoveItem(){

        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);

        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);

        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));

        cartService.removeItemFromCart("1", httpSession);

        assertEquals(0, shoppingCartViewModel.size());
    }

    @Test
    public void calcTotal_shouldReturnCorrectTotalSumOfCart(){

        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);

        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);

        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));

        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));

        assertEquals(BigDecimal.valueOf(10), cartService.calcTotal(httpSession));
    }

    @Test
    public void prepareOrder_shouldPrepareOrderForCreation(){

        User user = new User();
        user.setId("1");
        user.setUsername("User");
        customer.setUser(user);

        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);

        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);

        cartService.addItemToCart("1", 1, shoppingCartViewModel,
                modelMapper.map(customer, CustomerServiceModel.class));
        OrderServiceModel orderServiceModel = cartService.prepareOrder
                (httpSession, customer.getUser().getUsername());

        assertEquals(LocalDateTime.now().getMinute(), orderServiceModel.getMadeOn().getMinute());

    }

    @Test
    public void prepareOrder_shouldReturnNullIfThereArentProductsInTheCart(){

        User user = new User();
        user.setId("1");
        user.setUsername("User");
        customer.setUser(user);

        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);

        when(productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        List<ShoppingCartViewModel> shoppingCartViewModel = new ArrayList<>();
        httpSession.setAttribute("shopping-cart", shoppingCartViewModel);

        OrderServiceModel orderServiceModel = cartService.prepareOrder
                (httpSession, customer.getUser().getUsername());

        assertNull(orderServiceModel);
    }
}
