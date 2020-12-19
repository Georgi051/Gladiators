package project.gladiators.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.service.TrainingPlanService;
import project.gladiators.service.WorkoutService;
import project.gladiators.service.serviceModels.WorkoutServiceModel;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/workouts")
public class WorkoutController extends BaseController{
    private final WorkoutService workoutService;
    private final TrainingPlanService trainingPlanService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, TrainingPlanService trainingPlanService) {
        this.workoutService = workoutService;
        this.trainingPlanService = trainingPlanService;
    }

    @GetMapping("/add-workout-training-plan")
    @PageTitle("Add workout training plan")
    public ModelAndView addWorkoutToTrainingPlan(ModelAndView modelAndView, HttpSession session,
                                                 TrainingPlanBindingModel trainingPlanBindingModel) {

        modelAndView.addObject("workouts", this.workoutService.findAll().stream()
                .sorted(Comparator.comparing(WorkoutServiceModel::getName))
                .collect(Collectors.toList()));
        TrainingPlanBindingModel trainingPlan = (TrainingPlanBindingModel) session.getAttribute("trainingPlan");

        modelAndView.addObject("trainingPlan", trainingPlan);
        modelAndView.addObject("trainingPlanBindingModel", trainingPlanBindingModel);
        return super.view("/trainer/add-workout-training-plan", modelAndView);
    }

    @PostMapping("/add-workout-training-plan")
    public ModelAndView addWorkoutToTrainingPlanPost(TrainingPlanBindingModel trainingPlanBindingModel,
                                                     RedirectAttributes redirectAttributes,
                                                     HttpSession httpSession, Principal principal
                                                 ){

        this.trainingPlanService.addWorkoutsToTrainingPlanByDay(trainingPlanBindingModel, httpSession, principal);
        redirectAttributes.addFlashAttribute("statusMessage", "You created training plan successful");
        redirectAttributes.addFlashAttribute("statusCode", "successful");

        return super.redirect("/");

    }

}
