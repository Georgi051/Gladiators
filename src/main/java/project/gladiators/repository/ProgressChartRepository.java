package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.entities.ProgressChart;

@Repository
public interface ProgressChartRepository extends JpaRepository<ProgressChart, String> {
}
