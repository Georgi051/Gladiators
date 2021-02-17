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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.gladiators.exceptions.OrderNotFoundException;
import project.gladiators.model.entities.*;
import project.gladiators.model.enums.OrderStatus;
import project.gladiators.repository.*;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.gladiators.constants.ExceptionMessages.ORDER_NOT_FOUND;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    UserRepository userRepository;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    DeliveryRepository deliveryRepository;

    @MockBean
    CustomerTrainingPlanInfoRepository customerTrainingPlanInfoRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerTrainingPlanInfoService customerTrainingPlanInfoService;

    private OrderServiceModel orderServiceModel;
    private UserServiceModel userServiceModel;
    private DeliveryServiceModel deliveryServiceModel;
    private ProductServiceModel productServiceModel;
    private User user;
    private Customer customer;
    private CustomerTrainingPlanInfo customerTrainingPlanInfo;


    @BeforeEach
    void setUp() {
        orderServiceModel = new OrderServiceModel();
        orderServiceModel.setId("1");
        productServiceModel = new ProductServiceModel();
        productServiceModel.setId("1");
        productServiceModel.setManufacturerName("BSN");
        productServiceModel.setName("Training plan");
        productServiceModel.setQuantity(4);
        productServiceModel.setReviews(new ArrayList<>());

        OrderProductServiceModel orderProductServiceModel = new OrderProductServiceModel();
        orderProductServiceModel.setId("1");
        orderProductServiceModel.setProduct(productServiceModel);
        orderProductServiceModel.getProduct().setBuyingProductsQuantity(2);

        userServiceModel = new UserServiceModel();
        userServiceModel.setId("1");
        userServiceModel.setEmail("testEmail@gmail.com");
        userServiceModel.setUsername("testUserName");
        userServiceModel.setPassword(bCryptPasswordEncoder.encode("testPassWord"));

        orderServiceModel.setProducts(List.of(orderProductServiceModel));
        orderServiceModel.setCustomer(userServiceModel);
        orderServiceModel.setOrderStatus(OrderStatus.PENDING);

        user = this.modelMapper.map(userServiceModel, User.class);

        deliveryServiceModel = new DeliveryServiceModel();
        deliveryServiceModel.setFirstName("TestFirstName");
        deliveryServiceModel.setLastName("TestLastName");
        deliveryServiceModel.setPhoneNumber("0884556633");
        deliveryServiceModel.setAddress("Address");
        deliveryServiceModel.setZip(1000);
        deliveryServiceModel.setPayMethod("cash");

        customer = new Customer();
        customer.setId("1");
        customer.setUser(user);

        customerTrainingPlanInfo = new CustomerTrainingPlanInfo();
        customerTrainingPlanInfo.setId("1");
        customerTrainingPlanInfo.setTrainingPlan(null);
        customerTrainingPlanInfo.setCustomer(customer);

    }

    @Test
    void testCreateOrderWhenProductIsTrainingPlanShouldReturnCorrectResult() {
        Product product = modelMapper.map(productServiceModel, Product.class);
        product.setQuantity(5);
        product.setBuyingCounter(0);

        Delivery delivery = this.modelMapper.map(deliveryServiceModel,Delivery.class);

        when(deliveryRepository.saveAndFlush(any(Delivery.class)))
                .thenReturn(delivery);

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(this.productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        when(this.customerRepository.findCustomerByUser(any(User.class)))
                .thenReturn(customer);

        when(this.customerTrainingPlanInfoRepository.findByCustomer_Id("1"))
                .thenReturn(Optional.of(customerTrainingPlanInfo));

        when(this.userRepository.findUserByUsername(any(String.class)))
                .thenReturn(Optional.of(user));

        this.orderService.createOrder(orderServiceModel, "testUserName",
                deliveryServiceModel);

        verify(this.orderRepository).saveAndFlush(any());
    }

    @Test
    void testCreateOrderProductShouldReturnCorrectResult() {
        Product product = modelMapper.map(productServiceModel, Product.class);
        product.setQuantity(5);
        product.setBuyingCounter(0);

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        Delivery delivery = this.modelMapper.map(deliveryServiceModel,Delivery.class);
        when(deliveryRepository.saveAndFlush(any(Delivery.class)))
                .thenReturn(delivery);

        when(this.productRepository.findById("1"))
                .thenReturn(Optional.of(product));

        orderServiceModel.getProducts().get(0).getProduct().setName("productName");

        this.orderService.createOrder(orderServiceModel, "testUserName", deliveryServiceModel);

        verify(this.orderRepository).saveAndFlush(any());
    }

    @Test
    void testFindAllOrdersShouldReturnCorrectResult() {
        orderServiceModel.getProducts().get(0).getProduct().setName("productName");

        when(this.orderRepository.findAll())
                .thenReturn(List.of(this.modelMapper.map(orderServiceModel, Order.class)));

        List<OrderServiceModel> allOrders = this.orderService.findAllOrders();
        OrderServiceModel orderServiceModel = allOrders.get(0);

        Assertions.assertEquals(1, allOrders.size());
        Assertions.assertEquals("testUserName", orderServiceModel.getCustomer().getUsername());
        Assertions.assertEquals("productName", orderServiceModel.getProducts().get(0).getProduct().getName());
    }

    @Test
    void testFindOrderByCustomerShouldReturnCorrectResult() {
        orderServiceModel.getProducts().get(0).getProduct().setName("productName");
        when(this.orderRepository.findAllOrdersByCustomer_UsernameOrderByMadeOn(orderServiceModel.getCustomer().getUsername()))
                .thenReturn(List.of(this.modelMapper.map(orderServiceModel, Order.class)));
        List<OrderServiceModel> orderServiceModelList = this.orderService.findOrdersByCustomer("testUserName");

        Assertions.assertEquals(1, orderServiceModelList.size());
        Assertions.assertEquals("testUserName", orderServiceModel.getCustomer().getUsername());
        Assertions.assertEquals("productName", orderServiceModel.getProducts().get(0).getProduct().getName());
    }

    @Test
    void testFindOrdersByIdShouldReturnCorrectResult() {
        orderServiceModel.getProducts().get(0).getProduct().setDescription("dasdadasdadasdadasdasdasdasdasdas");

        when(this.orderRepository.findOrderById(orderServiceModel.getId()))
                .thenReturn(Optional.of(this.modelMapper.map(orderServiceModel, Order.class)));

        OrderServiceModel testOrder = this.orderService.findOrderById("1");

        Assertions.assertEquals(orderServiceModel.getId(), testOrder.getId());
        Assertions.assertEquals(orderServiceModel.getCustomer().getUsername(), testOrder.getCustomer().getUsername());
    }

    @Test
    void testFindOrdersByIdShouldReturnOrderNoFoundException() {
        Exception exception = Assertions.assertThrows(OrderNotFoundException.class, () -> {
            this.orderService.findOrderById("testId");
        });
        Assertions.assertEquals(exception.getMessage(), ORDER_NOT_FOUND);
    }

    @Test
    void testChangeOrderStatusFromPendingToActiveShouldReturnCorrectResult() {
        when(this.orderRepository.findById("1"))
                .thenReturn(Optional.of(this.modelMapper.map(orderServiceModel, Order.class)));
        this.orderService.changeOrderStatus("1");

        verify(orderRepository).save(any());
    }

    @Test
    void testChangeOrderStatusFromActiveToFinishedShouldReturnCorrectResult() {
        orderServiceModel.setOrderStatus(OrderStatus.ACTIVE);
        when(this.orderRepository.findById("1"))
                .thenReturn(Optional.of(this.modelMapper.map(orderServiceModel, Order.class)));
        this.orderService.changeOrderStatus("1");

        verify(orderRepository).save(any());
    }

    @Test
    void testChangeOrderStatusShouldThrowOrderNoFoundException() {
        Exception exception = Assertions.assertThrows(OrderNotFoundException.class, () -> {
            this.orderService.changeOrderStatus("testId");
        });
        Assertions.assertEquals(exception.getMessage(), ORDER_NOT_FOUND);
    }
}