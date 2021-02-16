package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.exceptions.OrderNotFoundException;
import project.gladiators.model.entities.Delivery;
import project.gladiators.model.entities.Order;
import project.gladiators.repository.DeliveryRepository;
import project.gladiators.repository.OrderRepository;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.DeliveryServiceModel;
import project.gladiators.service.serviceModels.OrderServiceModel;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static project.gladiators.constants.ExceptionMessages.ORDER_NOT_FOUND;
import static project.gladiators.model.enums.OrderStatus.*;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final UserService userService;
    private final DeliveryRepository deliveryRepository;
    private final CustomerService customerService;
    private final CustomerTrainingPlanInfoService customerTrainingPlanInfoService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, ProductService productService, UserService userService, DeliveryRepository deliveryRepository, CustomerService customerService, CustomerTrainingPlanInfoService customerTrainingPlanInfoService) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.userService = userService;
        this.deliveryRepository = deliveryRepository;
        this.customerService = customerService;
        this.customerTrainingPlanInfoService = customerTrainingPlanInfoService;
    }

    @Override
    @Transactional
    public void createOrder(OrderServiceModel orderServiceModel, String name, DeliveryServiceModel deliveryServiceModel) {
        this.productService.sellProduct(orderServiceModel.getProducts());
        Order order = this.modelMapper.map(orderServiceModel, Order.class);
        order.setOrderStatus(PENDING);
        order.getProducts().forEach(product ->{
            if (product.getProduct().getName().equals("Training plan")){
                CustomerServiceModel customer = customerService.findCustomerByUser(userService.findUserByUsername(name));
                customerTrainingPlanInfoService.customerPaidTrainingPlan(customer);
            }
        });

        Delivery delivery =
                this.deliveryRepository.saveAndFlush(this.modelMapper.map(deliveryServiceModel, Delivery.class));
        order.setDelivery(delivery);
        this.orderRepository.saveAndFlush(order);
    }

    @Override
    public List<OrderServiceModel> findAllOrders() {
        List<Order> orders = this.orderRepository.findAll();
        List<OrderServiceModel> orderServiceModels = orders
                .stream()
                .map(o -> this.modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());
        return orderServiceModels.stream().filter
                (orderServiceModel ->
                        orderServiceModel.getProducts().size() == 1 &&
                                !orderServiceModel.getProducts().get(0)
                                        .getProduct().getName().equals("Training plan")
                                || orderServiceModel.getProducts().size() > 1)
                .sorted(Comparator.comparing(OrderServiceModel::getMadeOn))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderServiceModel> findOrdersByCustomer(String username) {
        List<OrderServiceModel> order = this.orderRepository.findAllOrdersByCustomer_UsernameOrderByMadeOn(username)
                .stream()
                .map(o -> modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());
        return order.stream().filter
                (orderServiceModel ->
                        orderServiceModel.getProducts().size() == 1 &&
                                !orderServiceModel.getProducts().get(0)
                                        .getProduct().getName().equals("Training plan")
                                || orderServiceModel.getProducts().size() > 1)
                .collect(Collectors.toList());
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

    @Override
    public void changeOrderStatus(String id) {

        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));

        if(order.getOrderStatus().equals(PENDING)){
            order.setOrderStatus(ACTIVE);
        }else if(order.getOrderStatus().equals(ACTIVE)){
            order.setOrderStatus(FINISHED);
        }

        this.orderRepository.save(order);
    }
}
