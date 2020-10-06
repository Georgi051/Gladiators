package project.gladiators.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.gladiators.model.entities.Workout;
import project.gladiators.model.entities.WorkoutExerciseInfo;
import project.gladiators.repository.WorkoutRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Order(value = 6)
public class WorkoutInitialization implements CommandLineRunner {

    private final WorkoutRepository workoutRepository;

    public WorkoutInitialization(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(this.workoutRepository.count() == 0){
            Workout workout = new Workout();
            workout.setDuration(0);
            workout.setName("RESTING DAY");

            workout.setWorkoutExerciseInfos(new HashSet<>());
            workoutRepository.saveAndFlush(workout);
        }
    }
}
