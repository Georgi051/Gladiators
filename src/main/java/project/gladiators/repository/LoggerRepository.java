package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.Logger;

@Repository
public interface LoggerRepository extends JpaRepository<Logger,String> {
}
