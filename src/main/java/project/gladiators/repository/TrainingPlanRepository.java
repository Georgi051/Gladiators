package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Customer;
import project.gladiators.model.entities.TrainingPlan;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan,String> {

    TrainingPlan getByCustomers(Customer customer);
}
