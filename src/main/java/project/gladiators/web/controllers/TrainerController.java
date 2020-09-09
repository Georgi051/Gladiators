package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.bindingModels.TrainerRegisterBindingModel;
import project.gladiators.service.TrainerService;
import project.gladiators.service.serviceModels.TrainerServiceModel;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/trainers")
public class TrainerController  extends BaseController{
    private TrainerService trainerService;
    private ModelMapper modelMapper;

    @Autowired
    public TrainerController(TrainerService trainerService, ModelMapper modelMapper) {
        this.trainerService = trainerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/confirmation")
    public ModelAndView confirm(ModelAndView mav){
        mav.addObject("trainer",new TrainerRegisterBindingModel());
        return super.view("/trainer/trainer-confirm",mav);
    }

    @PostMapping("/confirmation")
    public ModelAndView confirm(@Valid TrainerRegisterBindingModel trainerRegisterBindingModel
            , BindingResult bindingResult
    , Principal principal){
        TrainerServiceModel trainerServiceModel=this.modelMapper.map(trainerRegisterBindingModel,TrainerServiceModel.class);
        trainerService.confirmTrainer(trainerServiceModel,principal.getName());
        return super.redirect("/home");
    }

}
