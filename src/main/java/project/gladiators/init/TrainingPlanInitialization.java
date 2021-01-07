package project.gladiators.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.gladiators.model.entities.*;
import project.gladiators.repository.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static project.gladiators.model.enums.TrainingPlanType.*;

@Component
@Order(value = 9)
public class TrainingPlanInitialization implements CommandLineRunner {

    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainerRepository trainerRepository;
    private final WorkoutRepository workoutRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final TrainingPlanWorkoutInfoRepository trainingPlanWorkoutInfoRepository;

    public TrainingPlanInitialization(TrainingPlanRepository trainingPlanRepository, TrainerRepository trainerRepository,
                                      WorkoutRepository workoutRepository, CustomerRepository customerRepository, UserRepository userRepository, TrainingPlanWorkoutInfoRepository trainingPlanWorkoutInfoRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.trainerRepository = trainerRepository;
        this.workoutRepository = workoutRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.trainingPlanWorkoutInfoRepository = trainingPlanWorkoutInfoRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if(trainingPlanRepository.count() == 0){

            Trainer trainer = this.trainerRepository
                    .findTrainerByUser_Username("trainer_confirmed").orElse(null);

            TrainingPlan trainingPlan = new TrainingPlan();
            trainingPlan.setName("Test Training Plan");
            trainingPlan.setStartedOn(LocalDate.now().plusDays(1));
            trainingPlan.setTrainingPlanType(LOST_WEIGHT);
            trainingPlan.setWorkouts(new ArrayList<>(7));
            DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
            for (int i = 0; i < 7; i++) {
                TrainingPlanWorkoutInfo trainingPlanWorkoutInfo = new TrainingPlanWorkoutInfo();
                trainingPlanWorkoutInfo.setDayOfWeek(dayOfWeek);
                if(i % 2 == 0){
                    trainingPlanWorkoutInfo.setWorkout(this.workoutRepository
                            .findAll().get(0));
                }else {
                    trainingPlanWorkoutInfo.setWorkout(this.workoutRepository
                            .findAll().get(1));
                }
                this.trainingPlanWorkoutInfoRepository.save(trainingPlanWorkoutInfo);
                trainingPlan.getWorkouts().add(trainingPlanWorkoutInfo);
                dayOfWeek = dayOfWeek.plus(1);
            }

            User user = this.userRepository.findUserByUsername("customer")
                    .orElse(null);
            Customer customer = this.customerRepository.findCustomerByUser(user);

            trainer.setCustomers(new HashSet<>());
            trainer.getCustomers().add(customer);
            trainer.setTrainingPlans(new ArrayList<>());
            trainingPlan.setCustomers(new HashSet<>());
            trainingPlan.getCustomers().add(customer);
            trainer.getTrainingPlans().add(trainingPlan);
            this.trainingPlanRepository.saveAndFlush(trainingPlan);
            this.trainerRepository.saveAndFlush(trainer);

        }
    }
}
