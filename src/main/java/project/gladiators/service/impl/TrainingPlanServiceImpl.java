package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.exceptions.TrainerNotFoundException;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.entities.*;
import project.gladiators.repository.*;
import project.gladiators.service.CustomerService;
import project.gladiators.service.TrainingPlanService;
import project.gladiators.service.WorkoutService;
import project.gladiators.service.serviceModels.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static project.gladiators.constants.ExceptionMessages.TRAINER_NOT_FOUND;

@Service
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final ModelMapper modelMapper;
    private final WorkoutService workoutService;
    private final CustomerService customerService;
    private final TrainerRepository trainerRepository;
    private final TrainingPlanWorkoutInfoRepository trainingPlanWorkoutInfoRepository;
    private final CustomerRepository customerRepository;
    private final CustomerTrainingPlanInfoRepository customerTrainingPlanInfoRepository;

    @Autowired
    public TrainingPlanServiceImpl(TrainingPlanRepository trainingPlanRepository, ModelMapper modelMapper, WorkoutService workoutService, CustomerService customerService, TrainerRepository trainerRepository, CustomerRepository customerRepository, TrainingPlanWorkoutInfoRepository trainingPlanWorkoutInfoRepository, CustomerTrainingPlanInfoRepository customerTrainingPlanInfoRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.modelMapper = modelMapper;
        this.workoutService = workoutService;
        this.customerService = customerService;
        this.trainerRepository = trainerRepository;
        this.trainingPlanWorkoutInfoRepository = trainingPlanWorkoutInfoRepository;
        this.customerRepository = customerRepository;
        this.customerTrainingPlanInfoRepository = customerTrainingPlanInfoRepository;
    }

    @Override
    public void addTrainingPlan(TrainingPlanServiceModel trainingPlanServiceModel, Principal principal) {

        Trainer trainer = this.trainerRepository
                .findTrainerByUser_Username(principal.getName())
                .orElse(null);

        TrainingPlan trainingPlan = this.modelMapper
                .map(trainingPlanServiceModel, TrainingPlan.class);
        trainer.getTrainingPlans()
                .add(trainingPlan);
        this.trainingPlanRepository.save(trainingPlan);
        this.trainingPlanWorkoutInfoRepository.saveAll(trainingPlan.getWorkouts());
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
            trainingPlanServiceModel.getWorkouts().add(new TrainingPlanWorkoutInfoServiceModel());

            trainingPlanServiceModel.getWorkouts().get(i).setDayOfWeek(dayOfWeek);
            WorkoutServiceModel workoutServiceModel = this.workoutService
                    .findById(trainingPlanBindingModel.getWorkout().get(i));

            Workout workout = this.modelMapper.map(workoutServiceModel, Workout.class);
            workout.setWorkoutExerciseInfos(new HashSet<>());
            workoutServiceModel.getWorkoutExerciseInfo().forEach(workoutExerciseInfoServiceModel -> {
                WorkoutExerciseInfo workoutExerciseInfo = this.modelMapper
                        .map(workoutExerciseInfoServiceModel, WorkoutExerciseInfo.class);
                workout.getWorkoutExerciseInfos().add(workoutExerciseInfo);
            });
            workoutServiceModel.setDayOfWeek(dayOfWeek);
            trainingPlanServiceModel.getWorkouts().get(i).setWorkout(workoutServiceModel);
            dayOfWeek = dayOfWeek.plus(1);
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
    public boolean addTrainingPlanToCustomer(String id, String name, String trainerName,
                                             LocalDate startedOn) {
        TrainingPlan trainingPlan = trainingPlanRepository.findByName(name);
        Customer findCustomerById = this.modelMapper.map(customerService.findCustomerById(id), Customer.class);

        CustomerTrainingPlanInfo customerTrainingPlanInfo = this.customerTrainingPlanInfoRepository.findByCustomer_Id(id);
        if (customerTrainingPlanInfo == null) {
            if(this.trainingPlanRepository.getByCustomers(findCustomerById) == null) {
                trainingPlan.getCustomers().add(findCustomerById);
                Trainer trainer = trainerRepository.findTrainerByUser_Username(trainerName)
                        .orElseThrow(() -> new TrainerNotFoundException(TRAINER_NOT_FOUND));
                    trainer.getCustomers().add(findCustomerById);
                    trainerRepository.save(trainer);
            }
            customerTrainingPlanInfo = new CustomerTrainingPlanInfo();
            customerTrainingPlanInfo.setTrainingPlan(trainingPlan);
            customerTrainingPlanInfo.setCustomer(findCustomerById);
            customerTrainingPlanInfo.setStartedOn(startedOn);
            customerTrainingPlanInfo.setPaid(false);
            customerTrainingPlanInfoRepository.save(customerTrainingPlanInfo);
            trainingPlanRepository.save(trainingPlan);
            return true;
        }
        return false;
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
