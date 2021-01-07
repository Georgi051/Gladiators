package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.entities.*;
import project.gladiators.repository.CustomerRepository;
import project.gladiators.repository.TrainerRepository;
import project.gladiators.repository.TrainingPlanRepository;
import project.gladiators.repository.TrainingPlanWorkoutInfoRepository;
import project.gladiators.service.CustomerService;
import project.gladiators.service.TrainingPlanService;
import project.gladiators.service.WorkoutService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;
import project.gladiators.service.serviceModels.WorkoutServiceModel;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.security.Principal;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final ModelMapper modelMapper;
    private final WorkoutService workoutService;
    private final CustomerService customerService;
    private final TrainerRepository trainerRepository;
    private final TrainingPlanWorkoutInfoRepository trainingPlanWorkoutInfoRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public TrainingPlanServiceImpl(TrainingPlanRepository trainingPlanRepository, ModelMapper modelMapper, WorkoutService workoutService, CustomerService customerService, TrainerRepository trainerRepository,CustomerRepository customerRepository, TrainingPlanWorkoutInfoRepository trainingPlanWorkoutInfoRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.modelMapper = modelMapper;
        this.workoutService = workoutService;
        this.customerService = customerService;
        this.trainerRepository = trainerRepository;
        this.trainingPlanWorkoutInfoRepository = trainingPlanWorkoutInfoRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void addTrainingPlan(TrainingPlanServiceModel trainingPlanServiceModel, Principal principal) {

        Trainer trainer = this.trainerRepository
                .findTrainerByUser_Username(principal.getName())
                .orElse(null);

        trainer.getTrainingPlans()
                .add(this.trainingPlanRepository.saveAndFlush(this.modelMapper
                                .map(trainingPlanServiceModel, TrainingPlan.class)));

        this.trainerRepository.saveAndFlush(trainer);
    }

    @Override
    public TrainingPlanServiceModel findById(String id) {
        return this.modelMapper
                .map(this.trainingPlanRepository
                .findById(id), TrainingPlanServiceModel.class);
    }

    @Override
    public void addWorkoutsToTrainingPlanByDay(TrainingPlanBindingModel trainingPlanBindingModel,
                                               HttpSession httpSession, Principal principal) {

        TrainingPlanBindingModel trainingPlan = (TrainingPlanBindingModel) httpSession.getAttribute("trainingPlan");
        TrainingPlanServiceModel trainingPlanServiceModel = this.modelMapper
                .map(trainingPlan, TrainingPlanServiceModel.class);
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        for (int i = 0; i < trainingPlanBindingModel.getWorkout().size(); i++) {
            trainingPlanServiceModel.getWorkouts().add(new TrainingPlanWorkoutInfo());

            trainingPlanServiceModel.getWorkouts().get(i).setDayOfWeek(dayOfWeek);
            dayOfWeek = dayOfWeek.plus(1);
            WorkoutServiceModel workoutServiceModel = this.workoutService
                    .findById(trainingPlanBindingModel.getWorkout().get(i));

            Workout workout = this.modelMapper.map(workoutServiceModel, Workout.class);
            workout.setWorkoutExerciseInfos(new HashSet<>());
            workoutServiceModel.getWorkoutExerciseInfo().forEach(workoutExerciseInfoServiceModel -> {
                WorkoutExerciseInfo workoutExerciseInfo = this.modelMapper
                        .map(workoutExerciseInfoServiceModel, WorkoutExerciseInfo.class);
                workout.getWorkoutExerciseInfos().add(workoutExerciseInfo);
            });

            trainingPlanServiceModel.getWorkouts().get(i).setWorkout(workout);
            this.trainingPlanWorkoutInfoRepository.saveAndFlush(trainingPlanServiceModel
            .getWorkouts().get(i));
        }
        this.addTrainingPlan(trainingPlanServiceModel, principal);


    }

    @Override
    public List<TrainingPlanServiceModel> findAll() {
        return this.trainingPlanRepository.findAll().stream()
                .map(trainingPlan -> this.modelMapper.map(trainingPlan,TrainingPlanServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addTrainingPlanToCustomer(String id, String name) {
        TrainingPlan trainingPlan = trainingPlanRepository.findByName(name);
        Customer findCustomerById = this.modelMapper.map(customerService.findCustomerById(id), Customer.class);
        trainingPlan.getCustomers().add(findCustomerById);
        trainingPlanRepository.save(trainingPlan);
    }

    @Override
    public TrainingPlanServiceModel findByCustomer(CustomerServiceModel customerServiceModel) {

        Customer customer = this.customerRepository.findById(customerServiceModel.getId())
                .orElse(null);

        TrainingPlan trainingPlan = this.trainingPlanRepository.getByCustomers(customer);

        if(trainingPlan != null){
            return this.modelMapper
                    .map(trainingPlan, TrainingPlanServiceModel.class);
        }else {
            return null;
        }
    }
}
