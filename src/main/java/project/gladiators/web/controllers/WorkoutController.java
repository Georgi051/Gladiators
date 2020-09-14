package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.entities.TrainingPlan;
import project.gladiators.model.enums.TrainingPlanType;
import project.gladiators.service.WorkoutService;
import project.gladiators.service.serviceModels.WorkoutServiceModel;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/workouts")
public class WorkoutController extends BaseController{
    private final WorkoutService workoutService;
    private final ModelMapper modelMapper;

    @Autowired
    public WorkoutController(WorkoutService workoutService, ModelMapper modelMapper) {
        this.workoutService = workoutService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add-workout-training-plan")
    @PageTitle("Add workout training plan")
    public ModelAndView addWorkoutToTrainingPlan(ModelAndView modelAndView, HttpSession session) {

        modelAndView.addObject("workouts", this.workoutService.findAll().stream()
                .sorted(Comparator.comparing(WorkoutServiceModel::getName))
                .collect(Collectors.toList()));
        TrainingPlanBindingModel trainingPlan = (TrainingPlanBindingModel) session.getAttribute("trainingPlan");
        int a = 5;
        return super.view("/trainer/add-training-plan", modelAndView);
    }

}
