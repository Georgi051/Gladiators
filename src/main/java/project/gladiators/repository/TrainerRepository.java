package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Trainer;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,String> {

    @Transactional
    void deleteTrainerByUser_Id(String id);

    @Transactional
    Optional<Trainer>findTrainerByUser_Username(String username);
}
