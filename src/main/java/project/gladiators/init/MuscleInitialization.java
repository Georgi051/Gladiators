package project.gladiators.init;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.gladiators.constants.GlobalConstants;
import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.model.entities.Muscle;
import project.gladiators.repository.MuscleRepository;

import java.io.FileReader;
import java.util.Arrays;

@Component
@Order(value = 3)
public class MuscleInitialization implements CommandLineRunner {
    private MuscleRepository muscleRepository;
    private Gson gson;

    @Autowired
    public MuscleInitialization(MuscleRepository muscleRepository, Gson gson) {
        this.muscleRepository = muscleRepository;
        this.gson = gson;
    }

    @Override
    public void run(String... args) throws Exception {
        if (muscleRepository.count() == 0) {
            Arrays.stream(this.gson.fromJson(new FileReader(GlobalConstants.MUSCLES_FILE_PATH), MuscleDto[].class))
                    .forEach(
                            e -> {
                                Muscle muscle = new Muscle();
                                muscle.setName(e.getName());
                                muscleRepository.save(muscle);
                            }
                    );
        }
    }
}
