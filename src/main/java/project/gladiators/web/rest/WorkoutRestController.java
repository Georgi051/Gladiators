package project.gladiators.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gladiators.model.entities.Workout;
import project.gladiators.service.WorkoutService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class WorkoutRestController {

    private final WorkoutService workoutService;
    private final ModelMapper modelMapper;

    public WorkoutRestController(WorkoutService workoutService, ModelMapper modelMapper) {
        this.workoutService = workoutService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/workouts/all")
    public List<Workout> getAllWorkouts(){

        List<Workout> workouts = workoutService.findAll()
                .stream()
                .map(workoutServiceModel -> {
                    Workout workout = this.modelMapper
                            .map(workoutServiceModel, Workout.class);
                    return workout;
                }).collect(Collectors.toList());

        return workouts;
    }
}
