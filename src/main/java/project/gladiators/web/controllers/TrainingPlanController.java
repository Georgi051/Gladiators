package project.gladiators.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.service.CustomerService;
import project.gladiators.service.CustomerTrainingPlanInfoService;
import project.gladiators.service.TrainingPlanService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.*;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trainingPlan")
public class TrainingPlanController extends BaseController{

    private final CustomerService customerService;
    private final UserService userService;
    private final TrainingPlanService trainingPlanService;
    private final CustomerTrainingPlanInfoService customerTrainingPlanInfoService;

    public TrainingPlanController(CustomerService customerService, UserService userService, TrainingPlanService trainingPlanService, CustomerTrainingPlanInfoService customerTrainingPlanInfoService) {
        this.customerService = customerService;
        this.userService = userService;
        this.trainingPlanService = trainingPlanService;
        this.customerTrainingPlanInfoService = customerTrainingPlanInfoService;
    }

    @GetMapping("/userId-{id}")
    @PageTitle("Your Training Plan")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ModelAndView getTrainingPlanByCustomerId(@PathVariable("id") String id,
                                                ModelAndView modelAndView){
        CustomerServiceModel customerServiceModel = this.customerService.findCustomerById(id);
        TrainingPlanServiceModel trainingPlan = this.trainingPlanService.findByCustomer(customerServiceModel);
        CustomerTrainingPlanInfoServiceModel customerTrainingPlanInfoServiceModel = this.customerTrainingPlanInfoService.findByCustomer(customerServiceModel);

        if (checkForCustomerTrainingPlan(customerTrainingPlanInfoServiceModel)){
            trainingPlan.setWorkouts(trainingPlan.getWorkouts().stream()
                    .sorted(Comparator.comparing(TrainingPlanWorkoutInfoServiceModel::getDayOfWeek))
                    .collect(Collectors.toList()));
            modelAndView.addObject("trainingPlan", trainingPlan);
            modelAndView.addObject("currentDayOfWeek", LocalDate.now().getDayOfWeek());
            return super.view("customer/customer-training-plan", modelAndView);
        }
        return super.redirect("/home");
    }

    @GetMapping("/workoutByDay-{dayOfWeek}")
    @PageTitle("Workout Info")
    public ModelAndView getWorkoutByDay(@PathVariable("dayOfWeek")DayOfWeek dayOfWeek,
                                        ModelAndView modelAndView, Principal principal) {

        UserServiceModel user = this.userService.findUserByUsername(principal.getName());
        CustomerServiceModel customer = this.customerService.findCustomerByUser(user);
        CustomerTrainingPlanInfoServiceModel customerTrainingPlanInfoServiceModel = this.customerTrainingPlanInfoService.findByCustomer(customer);

        if (checkForCustomerTrainingPlan(customerTrainingPlanInfoServiceModel)) {
            TrainingPlanServiceModel trainingPlan = this.trainingPlanService.findByCustomer(customer);
            trainingPlan.getWorkouts()
                    .forEach(workout -> {
                        if (workout.getWorkout().getDayOfWeek().equals(dayOfWeek)) {
                            modelAndView.addObject("workout", workout);
                            modelAndView.addObject("exercises", workout.getWorkout().getWorkoutExerciseInfo());
                        }
                    });
            return super.view("customer/workout-by-day", modelAndView);
        }
        return super.redirect("/home");

    }

    private boolean checkForCustomerTrainingPlan(CustomerTrainingPlanInfoServiceModel customerTrainingPlanInfoServiceModel) {
        return customerTrainingPlanInfoServiceModel != null && Period.between(customerTrainingPlanInfoServiceModel.getStartedOn(),
                LocalDate.now()).getDays() >= 0 && customerTrainingPlanInfoServiceModel.isPaid();
    }
}
