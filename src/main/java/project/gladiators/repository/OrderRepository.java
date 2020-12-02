package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {
    List<Order> findAllOrdersByCustomer_UsernameOrderByMadeOn(String username);
}
