package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.service.LoggerService;
import project.gladiators.service.serviceModels.AdminLogServiceModel;
import project.gladiators.web.viewModels.AdminLogViewModel;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/logger")
public class LoggerController extends BaseController {
    private final LoggerService loggerService;
    private final ModelMapper modelMapper;

    @Autowired
    public LoggerController(LoggerService loggerService, ModelMapper modelMapper) {
        this.loggerService = loggerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PageTitle("Logger")
    @PreAuthorize("hasRole('ROOT')")
    public ModelAndView addCategory(ModelAndView modelAndView) {
        modelAndView.addObject("logs",loggerService.findAll().stream()
        .map(adminLogServiceModel -> this.modelMapper.map(adminLogServiceModel, AdminLogViewModel.class))
        .collect(Collectors.toList()));
        return super.view("/admin-log",modelAndView);
    }

}
