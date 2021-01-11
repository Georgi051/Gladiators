package project.gladiators.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.gladiators.model.entities.Logger;
import project.gladiators.repository.LoggerRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
public class AdminLogScheduler {
    private final LoggerRepository loggerRepository;

    public AdminLogScheduler(LoggerRepository loggerRepository) {
        this.loggerRepository = loggerRepository;
    }

    @Scheduled(cron = "0 0 0 3 * ?")
    private void changeLoggerStatus() {
        List<Logger> loggerList = loggerRepository.findAll();
        loggerList.forEach(logger -> {
            int period = Period.between(logger.getMadeOn().toLocalDate(), LocalDate.now()).getDays();
            if (period > 5) {
                this.loggerRepository.delete(logger);
            }
        });
    }
}
