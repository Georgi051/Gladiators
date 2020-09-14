package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.WorkoutExerciseInfo;

@Repository
public interface WorkoutExerciseInfoRepository extends JpaRepository<WorkoutExerciseInfo,String> {
}
