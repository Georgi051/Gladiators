package project.gladiators.init;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.gladiators.constants.GlobalConstants;
import project.gladiators.model.dtos.ExerciseDto;
import project.gladiators.model.entities.Exercise;
import project.gladiators.model.entities.Muscle;
import project.gladiators.repository.ExerciseRepository;
import project.gladiators.repository.MuscleRepository;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Order(value = 4)
public class ExerciseInitialization implements CommandLineRunner {
    private ExerciseRepository exerciseRepository;
    private Gson gson;
    private ModelMapper modelMapper;
    private MuscleRepository muscleRepository;


    @Autowired
    public ExerciseInitialization(ExerciseRepository exerciseRepository, Gson gson, ModelMapper modelMapper, MuscleRepository muscleRepository) {
        this.exerciseRepository = exerciseRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.muscleRepository = muscleRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (exerciseRepository.count() == 0) {
            Arrays.stream(this.gson.fromJson(new FileReader(GlobalConstants.EXERCISES_FILE_PATH), ExerciseDto[].class))
                    .forEach(e -> {
                        Exercise exercise = this.modelMapper.map(e, Exercise.class);
                        List<Muscle> muscles = new ArrayList<>();
                        e.getMuscles().forEach(
                                m -> muscles.add(this.muscleRepository.findByName(m.getName()))
                        );
                        exercise.setMuscles(muscles);

                        exerciseRepository.save(exercise);
                    });
        }

    }
}
