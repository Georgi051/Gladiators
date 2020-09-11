package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.bindingModels.ExerciseEditBindingModel;
import project.gladiators.model.bindingModels.TrainerRegisterBindingModel;
import project.gladiators.model.bindingModels.WorkoutAddBindingModel;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.ExerciseServiceModel;
import project.gladiators.service.serviceModels.MuscleServiceModel;
import project.gladiators.service.serviceModels.TrainerServiceModel;
import project.gladiators.service.serviceModels.WorkoutServiceModel;
import project.gladiators.web.viewModels.ExerciseViewModel;
import project.gladiators.web.viewModels.MuscleViewModel;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trainers")
public class TrainerController extends BaseController {
    private final TrainerService trainerService;
    private final ExerciseService exerciseService;
    private final MuscleService muscleService;
    private final WorkoutService workoutService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public TrainerController(TrainerService trainerService, ExerciseService exerciseService, MuscleService muscleService, WorkoutService workoutService, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.trainerService = trainerService;
        this.exerciseService = exerciseService;
        this.muscleService = muscleService;
        this.workoutService = workoutService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/confirmation")
    public ModelAndView confirm(ModelAndView mav) {
        mav.addObject("trainer", new TrainerRegisterBindingModel());
        return super.view("/trainer/trainer-confirm", mav);
    }

    @PostMapping("/confirmation")
    public ModelAndView confirm(@Valid TrainerRegisterBindingModel trainerRegisterBindingModel
            , BindingResult bindingResult
            , Principal principal) {
        TrainerServiceModel trainerServiceModel = this.modelMapper.map(trainerRegisterBindingModel, TrainerServiceModel.class);
        trainerService.confirmTrainer(trainerServiceModel, principal.getName());
        return super.redirect("/home");
    }

    @GetMapping("/add-exercise")
    public ModelAndView addExercise(ModelAndView modelAndView) {
        modelAndView.addObject("exercise", new ExerciseEditBindingModel());
        modelAndView.addObject("muscles", this.muscleService.findAll().stream()
                .sorted(Comparator.comparing(MuscleServiceModel::getName))
                .map(muscleServiceModel -> this.modelMapper.map(muscleServiceModel, MuscleViewModel.class))
                .collect(Collectors.toList()));
        return super.view("/trainer/exercise-add", modelAndView);
    }

    @PostMapping("/add-exercise")
    public ModelAndView addExercise(@Valid @ModelAttribute(name = "exercise") ExerciseEditBindingModel exerciseEditBindingModel, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("exercise", exerciseEditBindingModel);
            modelAndView.addObject("muscles", this.muscleService.findAll().stream()
                    .sorted(Comparator.comparing(MuscleServiceModel::getName))
                    .map(muscleServiceModel -> this.modelMapper.map(muscleServiceModel, MuscleViewModel.class))
                    .collect(Collectors.toList()));
            modelAndView.addObject("org.springframework.validation.BindingResult.exercise", result);
            return super.view("/trainer/exercise-add", modelAndView);
        }
        String imageUrl = exerciseEditBindingModel.getImageUrl().isEmpty() ? "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg" : cloudinaryService.uploadImage(exerciseEditBindingModel.getImageUrl());
        ExerciseServiceModel exerciseServiceModel = this.modelMapper.map(exerciseEditBindingModel,ExerciseServiceModel.class);
        exerciseServiceModel.setImageUrl(imageUrl);
        this.exerciseService.addExercise(exerciseServiceModel);
        return super.redirect("/trainers/add-exercise");

    }

    @GetMapping("/add-workout")
    public ModelAndView addWorkout(ModelAndView modelAndView){

        modelAndView.addObject("workout", new WorkoutAddBindingModel());
        modelAndView.addObject("exercises", this.exerciseService.findAll().stream()
                .sorted(Comparator.comparing(ExerciseServiceModel::getName))
                .collect(Collectors.toList()));
        return super.view("/trainer/workout-add", modelAndView);
    }

    @PostMapping("/add-workout")
    public ModelAndView addWorkout(@Valid @ModelAttribute("workout")
                                   WorkoutAddBindingModel workoutAddBindingModel,
                                   BindingResult bindingResult,
                                   ModelAndView modelAndView){

        if(bindingResult.hasErrors()){
            modelAndView.addObject("workout", workoutAddBindingModel);
            modelAndView.addObject("exercises", this.exerciseService.findAll().stream()
                    .sorted(Comparator.comparing(ExerciseServiceModel::getName))
                    .collect(Collectors.toList()));
            return super.view("/trainer/workout-add", modelAndView);
        }

        WorkoutServiceModel workoutServiceModel = this.modelMapper
                .map(workoutAddBindingModel, WorkoutServiceModel.class);

        this.workoutService.addWorkout(workoutServiceModel);

        return super.redirect("/trainers/add-workout");

    }



}
