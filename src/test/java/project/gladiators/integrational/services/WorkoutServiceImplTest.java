package project.gladiators.integrational.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.entities.Exercise;
import project.gladiators.model.entities.TrainingPlan;
import project.gladiators.model.entities.Workout;
import project.gladiators.model.entities.WorkoutExerciseInfo;
import project.gladiators.model.enums.TrainingPlanType;
import project.gladiators.repository.WorkoutExerciseInfoRepository;
import project.gladiators.repository.WorkoutRepository;
import project.gladiators.service.WorkoutExerciseInfoService;
import project.gladiators.service.WorkoutService;
import project.gladiators.service.serviceModels.WorkoutExerciseInfoServiceModel;
import project.gladiators.service.serviceModels.WorkoutServiceModel;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WorkoutServiceImplTest {

    Workout workout;
    WorkoutExerciseInfo workoutExerciseInfo;
    Exercise exercise;
    TrainingPlan trainingPlan;

    @MockBean
    WorkoutRepository workoutRepository;

    @MockBean
    WorkoutExerciseInfoRepository workoutExerciseInfoRepository;

    @Autowired
    WorkoutService workoutService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    WorkoutExerciseInfoService workoutExerciseInfoService;

    @BeforeEach
    public void setUp(){
        workout = new Workout();
        workout.setId("1");
        workout.setWorkoutExerciseInfos(new HashSet<>());
        workout.setName("ABS");
        exercise = new Exercise();
        exercise.setId("1");
        exercise.setName("BENCH PRESS");
        exercise.setMuscles(new ArrayList<>());
        workoutExerciseInfo = new WorkoutExerciseInfo();
        workoutExerciseInfo.setExercise(exercise);
        workoutExerciseInfo.setSets(2);
        workoutExerciseInfo.setRestTime(2);
        workoutExerciseInfo.setRepeats("2");
        workout.getWorkoutExerciseInfos().add(workoutExerciseInfo);
        trainingPlan = new TrainingPlan();
        trainingPlan.setName("Test Training Plan");
        trainingPlan.setTrainingPlanType(TrainingPlanType.BUILD_MUSCLE);
    }

    @Test
    public void addWorkout_shouldSaveWorkout(){
        List<Exercise> exercises = new ArrayList<>();

        exercises.add(exercise);

        WorkoutServiceModel workoutServiceModel = modelMapper.map(workout, WorkoutServiceModel.class);
        workoutServiceModel.setWorkoutExerciseInfo(new ArrayList<>());
        workoutServiceModel.getWorkoutExerciseInfo().add(modelMapper.map(workoutExerciseInfo, WorkoutExerciseInfoServiceModel.class));
        workoutService.addWorkout(workoutServiceModel, exercises);

        when(workoutRepository.save(any()))
                .thenReturn(workout);
    }

    @Test
    public void addWorkoutToTrainingPlan_shouldAddWorkoutsToTrainingPlan(){

        TrainingPlanBindingModel trainingPlanBindingModel = modelMapper.map
                (trainingPlan, TrainingPlanBindingModel.class);
        WorkoutServiceModel workoutServiceModel = modelMapper.map
                (workout, WorkoutServiceModel.class);
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise);

        workoutService.addWorkoutToTrainingPlan(workoutServiceModel, exercises, trainingPlanBindingModel);

        assertEquals(1, trainingPlanBindingModel.getWorkout().size());
    }

    @Test
    public void findAll_shouldReturnCollectionsOfAllWorkouts(){
        when(workoutRepository.findAll())
                .thenReturn(List.of(workout));

        List<WorkoutServiceModel> workoutServiceModels = workoutService.findAll();

        assertEquals(1, workoutServiceModels.size());
    }

    @Test
    public void findById_shouldReturnCorrectWorkoutIfSuchExists(){
        when(workoutRepository.findById("1"))
                .thenReturn(Optional.of(workout));

        WorkoutServiceModel workoutServiceModel = workoutService.findById("1");

        assertEquals("ABS", workoutServiceModel.getName());
    }
}
