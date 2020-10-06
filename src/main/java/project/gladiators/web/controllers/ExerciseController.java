package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.entities.Exercise;
import project.gladiators.service.ExerciseService;
import project.gladiators.service.serviceModels.ExerciseServiceModel;
import project.gladiators.web.viewModels.ExerciseViewModel;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exercises")
public class ExerciseController extends BaseController {


}
