package project.gladiators.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.entities.TrainingPlanWorkoutInfo;
import project.gladiators.model.entities.WorkoutExerciseInfo;
import project.gladiators.service.CustomerService;
import project.gladiators.service.TrainingPlanService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.security.Principal;
import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trainingPlan")
public class TrainingPlanController extends BaseController{

    private final CustomerService customerService;
    private final UserService userService;
    private final TrainingPlanService trainingPlanService;

    public TrainingPlanController(CustomerService customerService, UserService userService, TrainingPlanService trainingPlanService) {
        this.customerService = customerService;
        this.userService = userService;
        this.trainingPlanService = trainingPlanService;
    }

    @GetMapping("/userId-{id}")
    @PageTitle("Your Training Plan")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ModelAndView getTrainingPlanByCustomerId(@PathVariable("id") String id,
                                                ModelAndView modelAndView){

        CustomerServiceModel customerServiceModel = this.customerService
                .findCustomerById(id);
        TrainingPlanServiceModel trainingPlan = this.trainingPlanService
                .findByCustomer(customerServiceModel);
        trainingPlan.setWorkouts(trainingPlan.getWorkouts().stream()
        .sorted(Comparator.comparing(TrainingPlanWorkoutInfo::getDayOfWeek)).collect(Collectors.toList()));
        modelAndView.addObject("trainingPlan", trainingPlan);

        return super.view("customer/customer-training-plan", modelAndView);
    }

    @GetMapping("/workoutByDay-{dayOfWeek}")
    @PageTitle("Workout Info")
    public ModelAndView getWorkoutByDay(@PathVariable("dayOfWeek")DayOfWeek dayOfWeek,
                                        ModelAndView modelAndView, Principal principal){

        UserServiceModel user = this.userService.findUserByUsername(principal.getName());
        CustomerServiceModel customer = this.customerService.findCustomerByUser(user);
        TrainingPlanServiceModel trainingPlan = this.trainingPlanService
                .findByCustomer(customer);
        final TrainingPlanWorkoutInfo[] trainingPlanWorkoutInfo = {new TrainingPlanWorkoutInfo()};
        trainingPlan.getWorkouts()
                .stream().filter(workoutInfo -> workoutInfo.getDayOfWeek().equals(dayOfWeek))
        .forEach(trainingPlanWorkoutInfo1 -> {
             trainingPlanWorkoutInfo[0] = trainingPlanWorkoutInfo1;
        });
        modelAndView.addObject("workout", trainingPlanWorkoutInfo[0]);
        modelAndView.addObject("exercises", trainingPlanWorkoutInfo[0].getWorkout().getWorkoutExerciseInfos());

        return super.view("customer/workout-by-day", modelAndView);
    }
}
