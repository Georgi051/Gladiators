package project.gladiators.integrational.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.model.bindingModels.WorkoutAddBindingModel;
import project.gladiators.model.entities.Exercise;
import project.gladiators.model.entities.WorkoutExerciseInfo;
import project.gladiators.repository.WorkoutExerciseInfoRepository;
import project.gladiators.service.WorkoutExerciseInfoService;
import project.gladiators.service.serviceModels.WorkoutExerciseInfoServiceModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseInfoServiceImplTest {

    WorkoutExerciseInfo workoutExerciseInfo;
    WorkoutExerciseInfo workoutExerciseInfo2;
    Set<WorkoutExerciseInfo> workouts;

    @MockBean
    WorkoutExerciseInfoRepository workoutExerciseInfoRepository;

    @Autowired
    WorkoutExerciseInfoService workoutExerciseInfoService;

    @BeforeEach
    public void setUp(){
        workoutExerciseInfo = new WorkoutExerciseInfo();
        workoutExerciseInfo2 = new WorkoutExerciseInfo();
        workouts = new HashSet<>();
        workouts.addAll(Set.of(workoutExerciseInfo, workoutExerciseInfo2));
    }

    @Test
    public void seedData_shouldAddListOfWorkoutsInTheRepo(){
        workoutExerciseInfoService.seedData(workouts);
        verify(workoutExerciseInfoRepository).saveAll(workouts);
    }

    @Test
    public void addWorkoutExerciseInfoParams_shouldSetInfoAboutTheWorkout(){
        WorkoutAddBindingModel workoutAddBindingModel = new WorkoutAddBindingModel();
        workoutAddBindingModel.setName("ABS");
        workoutAddBindingModel.setExercises(new ArrayList<>());
        workoutAddBindingModel.getExercises().addAll(List.of(new Exercise(), new Exercise()));
        workoutAddBindingModel.setRepeats(new ArrayList<>());
        workoutAddBindingModel.getRepeats().addAll(List.of("3", "2"));
        workoutAddBindingModel.setSets(new ArrayList<>());
        workoutAddBindingModel.getSets().addAll(List.of(4, 3));
        workoutAddBindingModel.setRestTime(new ArrayList<>());
        workoutAddBindingModel.getRestTime().addAll(List.of(3, 3));

        List<WorkoutExerciseInfoServiceModel> workoutExerciseInfoServiceModels =
                workoutExerciseInfoService.addWorkoutExerciseInfoParams(workoutAddBindingModel);

        assertEquals("3", workoutExerciseInfoServiceModels.get(0).getRepeats());

    }
}
