package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.TrainingPlanWorkoutInfo;

@Repository
public interface TrainingPlanWorkoutInfoRepository extends JpaRepository<TrainingPlanWorkoutInfo, String> {
}
