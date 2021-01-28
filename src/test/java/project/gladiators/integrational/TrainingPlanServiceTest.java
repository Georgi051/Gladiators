package project.gladiators.integrational;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.entities.*;
import project.gladiators.model.enums.TrainingPlanType;
import project.gladiators.repository.*;
import project.gladiators.service.TrainingPlanService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.TrainerServiceModel;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TrainingPlanServiceTest {

    TrainingPlan trainingPlan;
    Trainer trainer;
    User user;
    User customerUser;
    CustomerTrainingPlanInfo customerTrainingPlanInfo;
    Customer customer;

    @MockBean
    TrainingPlanRepository trainingPlanRepository;

    @MockBean
    TrainerRepository trainerRepository;

    @MockBean
    TrainingPlanWorkoutInfoRepository trainingPlanWorkoutInfoRepository;

    @MockBean
    WorkoutRepository workoutRepository;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    CustomerTrainingPlanInfoRepository customerTrainingPlanInfoRepository;

    @Autowired
    TrainingPlanService trainingPlanService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    HttpSession httpSession;

    @BeforeEach
    public void setUp(){
        trainingPlan = new TrainingPlan();
        trainingPlan.setId("1");
        trainingPlan.setName("Test Training Plan");
        trainer = new Trainer();
        trainer.setId("1");
        trainer.setTrainingPlans(new ArrayList<>());
        trainer.setCustomers(new HashSet<>());
        user = new User();
        user.setId("1");
        user.setUsername("Trainer");
        trainer.setUser(user);

        customerUser = new User();
        customerUser.setId("2");
        customerUser.setUsername("Customer");
        customer = new Customer();
        customer.setId("1");
        customer.setUser(customerUser);

        customerTrainingPlanInfo = new CustomerTrainingPlanInfo();
        customerTrainingPlanInfo.setId("1");
        customerTrainingPlanInfo.setCustomer(customer);
        customerTrainingPlanInfo.setTrainingPlan(trainingPlan);
    }

    @Test
    public void addTrainingPlan_shouldCreateTrainingPlan(){
        when(trainerRepository.findTrainerByUser_Username("Trainer"))
                .thenReturn(Optional.of(trainer));

        TrainingPlanServiceModel trainingPlanServiceModel = modelMapper.map(trainingPlan, TrainingPlanServiceModel.class);
        trainingPlanService.addTrainingPlan(trainingPlanServiceModel, "Trainer");

        verify(trainingPlanRepository).save(any());
    }

    @Test
    public void findById_shouldReturnCorrectTrainingPlanIfSuchExists(){
        when(trainingPlanRepository.findById("1"))
                .thenReturn(Optional.of(trainingPlan));

        TrainingPlanServiceModel trainingPlanServiceModel = trainingPlanService.findById("1");

        assertEquals("Test Training Plan", trainingPlanServiceModel.getName());

    }

    @Test
    public void addWorkoutsToTrainingPlanByDay_shouldCreateTrainingPlanWithWorkouts(){
        when(trainerRepository.findTrainerByUser_Username("Trainer"))
                .thenReturn(Optional.of(trainer));
        Workout workout = new Workout();
        workout.setName("ABS");
        workout.setId("1");
        workout.setWorkoutExerciseInfos(new HashSet<>());
        WorkoutExerciseInfo workoutExerciseInfo = new WorkoutExerciseInfo();
        workoutExerciseInfo.setId("1");
        workoutExerciseInfo.setRepeats("3");
        workoutExerciseInfo.setSets(3);
        workoutExerciseInfo.setRestTime(3);
        workout.getWorkoutExerciseInfos().add(workoutExerciseInfo);
        Exercise exercise = new Exercise();
        exercise.setId("1");
        exercise.setName("BENCH PRESS");
        workoutExerciseInfo.setExercise(exercise);
        when(workoutRepository.findById("1"))
                .thenReturn(Optional.of(workout));

        TrainingPlanBindingModel trainingPlanBindingModel = modelMapper.map(
                trainingPlan, TrainingPlanBindingModel.class);
        trainingPlanBindingModel.setWorkout(new ArrayList<>());
        trainingPlanBindingModel.getWorkout().add("1");
        trainingPlanBindingModel.getWorkout().add("1");
        trainingPlanBindingModel.getWorkout().add("1");
        trainingPlanBindingModel.getWorkout().add("1");
        trainingPlanBindingModel.getWorkout().add("1");
        trainingPlanBindingModel.getWorkout().add("1");
        trainingPlanBindingModel.getWorkout().add("1");
        trainingPlanBindingModel.setStartedOn(LocalDate.now());
        trainingPlanBindingModel.setTrainingPlanType(TrainingPlanType.BUILD_MUSCLE);

        httpSession.setAttribute("trainingPlan", modelMapper.map(trainingPlan, TrainingPlanBindingModel.class));
        trainingPlanService.addWorkoutsToTrainingPlanByDay(trainingPlanBindingModel, httpSession, "Trainer");

        verify(trainingPlanRepository).save(any());
    }

    @Test
    public void findAll_shouldReturnAllTrainingPlansIfThereAreTrainingPlans(){
        List<TrainingPlan> trainingPlans = new ArrayList<>();
        trainingPlans.add(trainingPlan);

        when(trainingPlanRepository.findAll())
                .thenReturn(trainingPlans);
        List<TrainingPlanServiceModel> trainingPlanServiceModels = trainingPlanService.findAll();

        assertEquals(trainingPlans.size(), trainingPlanServiceModels.size());
    }

    @Test
    public void addTrainingPlanToCustomer_shouldAddGivenTrainingPlanToGivenCustomer(){

        when(trainingPlanRepository.findByName("Test Training Plan"))
                .thenReturn(trainingPlan);
        when(customerRepository.findFirstById("1"))
                .thenReturn(customer);
        when(trainerRepository.findTrainerByUser_Username("Trainer"))
                .thenReturn(Optional.of(trainer));
        trainingPlanService.addTrainingPlanToCustomer(customer.getId(), trainingPlan.getName(), trainer.getUser().getUsername(),
                LocalDate.now());

        verify(customerTrainingPlanInfoRepository).save(any());
    }

    @Test
    public void addTrainingPlanToCustomer_shouldReturnFalseIfCustomerAlreadyHaveTrainingPlan(){

        when(trainingPlanRepository.findByName("Test Training Plan"))
                .thenReturn(trainingPlan);
        when(customerTrainingPlanInfoRepository.findByCustomer_Id("1"))
                .thenReturn(Optional.of(customerTrainingPlanInfo));
        when(customerRepository.findFirstById("1"))
                .thenReturn(customer);
        when(trainerRepository.findTrainerByUser_Username("Trainer"))
                .thenReturn(Optional.of(trainer));

        assertFalse(trainingPlanService.addTrainingPlanToCustomer(customer.getId(), trainingPlan.getName(), trainer.getUser().getUsername(),
                LocalDate.now()));
    }

    @Test
    public void findByCustomer_shouldReturnTrainingPlanByCustomerIfSuchExists(){

        when(customerTrainingPlanInfoRepository.findByCustomer_Id("1"))
                .thenReturn(Optional.of(customerTrainingPlanInfo));
        when(customerRepository.findById("1"))
                .thenReturn(Optional.of(customer));
        when(trainingPlanRepository.getByCustomers(customer))
                .thenReturn(trainingPlan);
        when(trainerRepository.findTrainerByUser_Username("Trainer"))
                .thenReturn(Optional.of(trainer));

        TrainingPlanServiceModel trainingPlanServiceModel = trainingPlanService.findByCustomer
                (modelMapper.map(customer, CustomerServiceModel.class));

        assertEquals("1", trainingPlanServiceModel.getId());
    }

    @Test
    public void findByCustomer_shouldReturnNullIfThisCustomerThereArentTrainingPLan(){

        when(customerTrainingPlanInfoRepository.findByCustomer_Id("1"))
                .thenReturn(Optional.of(customerTrainingPlanInfo));
        when(customerRepository.findById("1"))
                .thenReturn(Optional.of(customer));
        when(trainingPlanRepository.getByCustomers(customer))
                .thenReturn(trainingPlan);
        when(trainerRepository.findTrainerByUser_Username("Trainer"))
                .thenReturn(Optional.of(trainer));

        assertNull(trainingPlanService.findByCustomer
                (new CustomerServiceModel()));
    }
}
