package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Workout;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, String> {
}
