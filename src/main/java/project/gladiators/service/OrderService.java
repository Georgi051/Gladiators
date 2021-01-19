package project.gladiators.service;

import project.gladiators.service.serviceModels.OrderServiceModel;

import java.util.List;

public interface OrderService {
    void createOrder(OrderServiceModel orderServiceModel, String name);

    List<OrderServiceModel> findAllOrders();

    List<OrderServiceModel> findOrdersByCustomer(String username);

    OrderServiceModel findOrderById(String id);

    void changeOrderStatus(String id);
}
