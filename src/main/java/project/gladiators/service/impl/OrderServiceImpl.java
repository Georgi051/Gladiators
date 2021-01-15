package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.exceptions.OrderNotFoundException;
import project.gladiators.model.entities.Order;
import project.gladiators.repository.OrderRepository;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.CustomerTrainingPlanInfoServiceModel;
import project.gladiators.service.serviceModels.OrderServiceModel;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static project.gladiators.constants.ExceptionMessages.ORDER_NOT_FOUND;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final UserService userService;
    private final CustomerService customerService;
    private final CustomerTrainingPlanInfoService customerTrainingPlanInfoService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, ProductService productService, UserService userService, CustomerService customerService, CustomerTrainingPlanInfoService customerTrainingPlanInfoService) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.userService = userService;
        this.customerService = customerService;
        this.customerTrainingPlanInfoService = customerTrainingPlanInfoService;
    }

    @Override
    @Transactional
    public void createOrder(OrderServiceModel orderServiceModel, String name) {
        this.productService.sellProduct(orderServiceModel.getProducts());
        Order order = this.modelMapper.map(orderServiceModel, Order.class);
        order.getProducts().forEach(product ->{
            if (product.getProduct().getName().equals("Training plan")){
                CustomerServiceModel customer = customerService.findCustomerByUser(userService.findUserByUsername(name));
                customerTrainingPlanInfoService.customerPaidTrainingPlan(customer);
            }
        });
        this.orderRepository.saveAndFlush(order);
    }

    @Override
    public List<OrderServiceModel> findAllOrders() {
        List<Order> orders = this.orderRepository.findAll();
        List<OrderServiceModel> orderServiceModels = orders
                .stream()
                .map(o -> this.modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());

        return orderServiceModels;
    }

    @Override
    public List<OrderServiceModel> findOrdersByCustomer(String username) {
        List<OrderServiceModel> order = this.orderRepository.findAllOrdersByCustomer_UsernameOrderByMadeOn(username)
                .stream()
                .map(o -> modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());
        return order;
    }

    @Override
    public OrderServiceModel findOrderById(String id) {
        Order order = this.orderRepository.findOrderById(id).orElseThrow(() ->
                new OrderNotFoundException(ORDER_NOT_FOUND));
        order.getProducts().forEach(orderProduct -> {
            if(orderProduct.getProduct().getDescription().length() > 20){
                orderProduct.getProduct().setDescription(orderProduct.getProduct()
                .getDescription().substring(0, 19).concat("..."));
            }
        });
        return modelMapper.map(order, OrderServiceModel.class);
    }
}
