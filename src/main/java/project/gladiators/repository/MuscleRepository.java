package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Muscle;

@Repository
public interface MuscleRepository extends JpaRepository<Muscle,String> {
}
