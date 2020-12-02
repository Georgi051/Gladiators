package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.exceptions.OrderNotFoundException;
import project.gladiators.model.entities.Order;
import project.gladiators.repository.OrderRepository;
import project.gladiators.service.OrderService;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.OrderServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static project.gladiators.constants.ExceptionMessages.ORDER_NOT_FOUND;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, ProductService productService) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @Override
    public void createOrder(OrderServiceModel orderServiceModel) {
        orderServiceModel.setMadeOn(LocalDateTime.now());
        List<ProductServiceModel> products = orderServiceModel.getProducts();
        productService.sellProduct(products);

        Order order = this.modelMapper.map(orderServiceModel, Order.class);
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
        OrderServiceModel order = this.orderRepository.findById(id)
                .map(o -> this.modelMapper.map(o, OrderServiceModel.class))
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        return order;
    }
}