package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Customer;
import project.gladiators.model.entities.User;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {
    Customer findCustomerByUser(User user);

    Customer findFirstById (String id);
}
