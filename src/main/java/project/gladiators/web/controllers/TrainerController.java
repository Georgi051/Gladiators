package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.exceptions.TrainerNotFoundException;
import project.gladiators.model.bindingModels.ExerciseAddBindingModel;
import project.gladiators.model.bindingModels.TrainerRegisterBindingModel;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.bindingModels.WorkoutAddBindingModel;
import project.gladiators.model.enums.TrainingPlanType;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.*;
import project.gladiators.validators.trainer.AddExerciseValidator;
import project.gladiators.validators.trainer.AddTrainingPlanValidator;
import project.gladiators.validators.trainer.AddWorkoutValidator;
import project.gladiators.validators.trainer.TrainerRegisterValidator;
import project.gladiators.web.viewModels.MuscleViewModel;
import project.gladiators.web.viewModels.TrainerViewModel;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trainers")
public class TrainerController extends BaseController {

    private final TrainerService trainerService;
    private final ExerciseService exerciseService;
    private final MuscleService muscleService;
    private final WorkoutService workoutService;
    private final WorkoutExerciseInfoService workoutExerciseInfoService;
    private final ModelMapper modelMapper;
    private final AddExerciseValidator addExerciseValidator;
    private final AddWorkoutValidator addWorkoutValidator;
    private final AddTrainingPlanValidator addTrainingPlanValidator;
    private final TrainerRegisterValidator trainerRegisterValidator;

    @Autowired
    public TrainerController(TrainerService trainerService, ExerciseService exerciseService, MuscleService muscleService, WorkoutService workoutService, WorkoutExerciseInfoService workoutExerciseInfoService, ModelMapper modelMapper, AddExerciseValidator addExerciseValidator, AddWorkoutValidator addWorkoutValidator, AddTrainingPlanValidator addTrainingPlanValidator, TrainerRegisterValidator trainerRegisterValidator) {
        this.trainerService = trainerService;
        this.exerciseService = exerciseService;
        this.muscleService = muscleService;
        this.workoutService = workoutService;
        this.workoutExerciseInfoService = workoutExerciseInfoService;
        this.modelMapper = modelMapper;
        this.addExerciseValidator = addExerciseValidator;
        this.addWorkoutValidator = addWorkoutValidator;
        this.addTrainingPlanValidator = addTrainingPlanValidator;
        this.trainerRegisterValidator = trainerRegisterValidator;
    }

    @PreAuthorize("hasRole('TRAINER_UNCONFIRMED')")
    @GetMapping("/confirmation")
    @PageTitle("Trainer confirmation")
    public ModelAndView confirm(ModelAndView modelAndView) {
        modelAndView.addObject("trainer", new TrainerRegisterBindingModel());
        return super.view("/trainer/trainer-confirm", modelAndView);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @PageTitle("All trainers")
    public ModelAndView getAllTrainers(ModelAndView modelAndView) {

        List<TrainerViewModel> trainers;
        try {
            trainers = List.of(this.modelMapper
                    .map(trainerService.findAll(), TrainerViewModel[].class));
            modelAndView.addObject("trainers", trainers);
        } catch (TrainerNotFoundException ex) {
            modelAndView.addObject("error", ex.getMessage());
        }

        return super.view("/trainer/all-trainers", modelAndView);
    }

    @PreAuthorize("hasRole('TRAINER_UNCONFIRMED')")
    @PostMapping("/confirmation")
    @PageTitle("Trainer confirmation")
    public ModelAndView confirm(@Valid @ModelAttribute("trainer") TrainerRegisterBindingModel trainerRegisterBindingModel
            , BindingResult bindingResult
            , Principal principal, ModelAndView modelAndView) throws IOException {

        trainerRegisterValidator.validate(trainerRegisterBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("org.springframework.validation.BindingResult.trainer", bindingResult);
            return super.view("/trainer/trainer-confirm", modelAndView);
        }

        TrainerServiceModel trainerServiceModel = this.modelMapper.map(trainerRegisterBindingModel, TrainerServiceModel.class);
        UserServiceModel userServiceModel = this.modelMapper.map(trainerRegisterBindingModel, UserServiceModel.class);
        trainerService.confirmTrainer(trainerServiceModel, userServiceModel, principal.getName(), trainerRegisterBindingModel.getImageUrl());
        return super.redirect("/home");
    }

    @GetMapping("/add-exercise")
    @PageTitle("Add exercise")
    public ModelAndView addExercise(ModelAndView modelAndView) {
        modelAndView.addObject("exercise", new ExerciseAddBindingModel());
        modelAndView.addObject("muscles", this.muscleService.findAll().stream()
                .sorted(Comparator.comparing(MuscleServiceModel::getName))
                .map(muscleServiceModel -> this.modelMapper.map(muscleServiceModel, MuscleViewModel.class))
                .collect(Collectors.toList()));
        return super.view("/trainer/exercise-add", modelAndView);
    }

    @PostMapping("/add-exercise")
    public ModelAndView addExercise(@Valid @ModelAttribute(name = "exercise") ExerciseAddBindingModel exerciseAddBindingModel, BindingResult result,
                                    RedirectAttributes redirectAttributes) throws IOException {
        addExerciseValidator.validate(exerciseAddBindingModel, result);
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("exercise", exerciseAddBindingModel);
            modelAndView.addObject("muscles", this.muscleService.findAll().stream()
                    .sorted(Comparator.comparing(MuscleServiceModel::getName))
                    .map(muscleServiceModel -> this.modelMapper.map(muscleServiceModel, MuscleViewModel.class))
                    .collect(Collectors.toList()));
            modelAndView.addObject("org.springframework.validation.BindingResult.exercise", result);
            return super.view("/trainer/exercise-add", modelAndView);
        }
        ExerciseServiceModel exerciseServiceModel = this.modelMapper.map(exerciseAddBindingModel, ExerciseServiceModel.class);
        this.exerciseService.addExercise(exerciseServiceModel, exerciseAddBindingModel.getImageUrl());

        redirectAttributes.addFlashAttribute("statusMessage", "You created exercise successful");
        redirectAttributes.addFlashAttribute("statusCode", "successful");

        return super.redirect("/trainers/add-exercise");
    }

    @GetMapping("/add-workout")
    @PageTitle("Add workout")
    public ModelAndView addWorkout(ModelAndView modelAndView) {

        modelAndView.addObject("workout", new WorkoutAddBindingModel());
        modelAndView.addObject("exercises", this.exerciseService.findAll().stream()
                .sorted(Comparator.comparing(ExerciseServiceModel::getName))
                .collect(Collectors.toList()));
        modelAndView.addObject("daysOfWeek", DayOfWeek.values());
        return super.view("/trainer/workout-add", modelAndView);
    }

    @PostMapping("/add-workout")
    public ModelAndView addWorkout(@Valid @ModelAttribute("workout")
                                           WorkoutAddBindingModel workoutAddBindingModel,
                                   BindingResult bindingResult,
                                   ModelAndView modelAndView,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {
        addWorkoutValidator.validate(workoutAddBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("workout", workoutAddBindingModel);
            modelAndView.addObject("exercises", this.exerciseService.findAll().stream()
                    .sorted(Comparator.comparing(ExerciseServiceModel::getName))
                    .collect(Collectors.toList()));
            modelAndView.addObject("daysOfWeek", DayOfWeek.values());
            return super.view("/trainer/workout-add", modelAndView);
        }

        WorkoutServiceModel workoutServiceModel = this.modelMapper
                .map(workoutAddBindingModel, WorkoutServiceModel.class);
        List<WorkoutExerciseInfoServiceModel> workoutExerciseModels = this.workoutExerciseInfoService
                .addWorkoutExerciseInfoParams(workoutAddBindingModel);
        workoutServiceModel.setWorkoutExerciseInfo(workoutExerciseModels);

        if(session.getAttribute("trainingPlan") != null){
            TrainingPlanBindingModel trainingPlan = (TrainingPlanBindingModel) session.getAttribute("trainingPlan");
            this.workoutService.addWorkoutToTrainingPlan(workoutServiceModel, workoutAddBindingModel.getExercises(), trainingPlan);
            modelAndView.addObject("workouts", this.workoutService.findAll().stream()
                    .sorted(Comparator.comparing(WorkoutServiceModel::getName))
                    .collect(Collectors.toList()));
            modelAndView.addObject("trainingPlan", trainingPlan);
            createWorkout(workoutAddBindingModel, redirectAttributes, workoutServiceModel);
            return super.view("/trainer/add-workout-training-plan", modelAndView);
        }
        createWorkout(workoutAddBindingModel, redirectAttributes, workoutServiceModel);
        return super.redirect("/trainers/add-workout");
    }

    @GetMapping("/add-training-plan")
    @PageTitle("Add training plan")
    public ModelAndView addTrainingPlan(ModelAndView modelAndView) {

        modelAndView.addObject("trainingPlan", new TrainingPlanBindingModel());
        modelAndView.addObject("workouts", this.workoutService.findAll().stream()
                .sorted(Comparator.comparing(WorkoutServiceModel::getName))
                .collect(Collectors.toList()));
        modelAndView.addObject("trainingPlanTypes", List.of(TrainingPlanType.values()));
        return super.view("/trainer/add-training-plan", modelAndView);
    }

    @PostMapping("/add-training-plan")
    public ModelAndView addTrainingPlan(@Valid @ModelAttribute("trainingPlan")
                                           TrainingPlanBindingModel trainingPlan,
                                   BindingResult bindingResult,
                                   ModelAndView modelAndView,
                                   HttpSession session) {
        addTrainingPlanValidator.validate(trainingPlan, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("trainingPlan", trainingPlan);
            modelAndView.addObject("workouts", this.workoutService.findAll().stream()
                    .sorted(Comparator.comparing(WorkoutServiceModel::getName))
                    .collect(Collectors.toList()));
            modelAndView.addObject("trainingPlanTypes", List.of(TrainingPlanType.values()));
            return super.view("/trainer/add-training-plan", modelAndView);
        }
        session.setAttribute("trainingPlan",trainingPlan);
        return super.redirect("/workouts/add-workout-training-plan");
    }

    private void createWorkout(@ModelAttribute("workout") @Valid WorkoutAddBindingModel workoutAddBindingModel, RedirectAttributes redirectAttributes, WorkoutServiceModel workoutServiceModel) {
        this.workoutService.addWorkout(workoutServiceModel, workoutAddBindingModel.getExercises());
        redirectAttributes.addFlashAttribute("statusMessage", "You created workout successful");
        redirectAttributes.addFlashAttribute("statusCode", "successful");
    }

}
