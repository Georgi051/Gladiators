package project.gladiators.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.entities.Exercise;
import project.gladiators.service.ExerciseService;
import project.gladiators.service.serviceModels.ExerciseServiceModel;
import project.gladiators.web.controllers.BaseController;
import project.gladiators.web.viewModels.ExerciseViewModel;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ExerciseRestController extends BaseController {

    private final ExerciseService exerciseService;
    private final ModelMapper modelMapper;

    @Autowired
    public ExerciseRestController(ExerciseService exerciseService, ModelMapper modelMapper) {
        this.exerciseService = exerciseService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/exercises/all")
    public Set<Exercise> getAllExercises(){

        Set<Exercise> exercises = this.exerciseService
                .findAll()
                .stream()
                .map(exerciseServiceModel -> {
                    Exercise exercise = this.modelMapper
                            .map(exerciseServiceModel, Exercise.class);
                    return exercise;
                }).collect(Collectors.toSet());
        return exercises;

    }


}
