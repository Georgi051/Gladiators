package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,String> {

}
