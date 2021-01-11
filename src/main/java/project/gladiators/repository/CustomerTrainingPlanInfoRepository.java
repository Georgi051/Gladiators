package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.CustomerTrainingPlanInfo;

@Repository
public interface CustomerTrainingPlanInfoRepository extends JpaRepository<CustomerTrainingPlanInfo, String> {

    CustomerTrainingPlanInfo findByCustomer_Id(String id);
}
