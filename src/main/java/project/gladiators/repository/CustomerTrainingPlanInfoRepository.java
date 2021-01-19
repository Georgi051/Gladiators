package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.CustomerTrainingPlanInfo;

import java.util.Optional;

@Repository
public interface CustomerTrainingPlanInfoRepository extends JpaRepository<CustomerTrainingPlanInfo, String> {

    Optional<CustomerTrainingPlanInfo> findByCustomer_Id(String id);
}
