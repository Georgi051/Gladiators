package project.gladiators.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.gladiators.model.entities.Order;
import project.gladiators.model.entities.OrderProduct;
import project.gladiators.repository.OrderProductRepository;
import project.gladiators.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

import static project.gladiators.model.enums.OrderStatus.*;

@Component
public class OrderScheduler {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderScheduler(OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Scheduled(cron = "0 0 0 5 * *")
    private void deleteOrders(){

        List<Order> orders = orderRepository.findAllByOrderStatus(FINISHED);
        orders.forEach(order -> {
            order.setProducts(new ArrayList<>());
            orderRepository.deleteById(order.getId());
        });

    }
}
