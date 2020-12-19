package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.bindingModels.SendMessageBindingModel;
import project.gladiators.service.MessageService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.MessageServiceModel;
import project.gladiators.service.serviceModels.TrainerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.web.viewModels.MessageViewModel;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/messages")
public class MessageController extends BaseController{

    private final MessageService messageService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public MessageController(MessageService messageService, ModelMapper modelMapper, UserService userService) {
        this.messageService = messageService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView messageInfo(@RequestParam("id") String id,
                                    ModelAndView modelAndView){

        modelAndView.addObject("message", this.messageService.getMessageInfo(id));
        return super.view("user/message-info", modelAndView);
    }

    @GetMapping("/delete")
    public ModelAndView deleteMessage(@RequestParam("id") String id){

        this.messageService.deleteMessageById(id);

        return redirect("/");
    }

    @GetMapping("/reply/")
    public ModelAndView sendMessage(@RequestParam("id") String id,
                                       ModelAndView modelAndView){

        UserServiceModel userServiceModel = this.userService.findById(id);
        SendMessageBindingModel sendMessageBindingModel = new SendMessageBindingModel();
        sendMessageBindingModel.setMessageTo(id);

        modelAndView.addObject("user", userServiceModel);
        modelAndView.addObject("sendMessageBindingModel", sendMessageBindingModel);
        return super.view("user/send-message", modelAndView);
    }

    @PostMapping("/reply/")
    public ModelAndView sendMessage(@RequestParam("id") String id,
                                    @Valid @ModelAttribute("sendMessageBindingModel")
                                                SendMessageBindingModel sendMessageBindingModel,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView,
                                    Principal principal){

        if(bindingResult.hasErrors()){
            sendMessageBindingModel.setMessageTo(id);
            modelAndView.addObject("sendMessageBindingModel", sendMessageBindingModel);
            UserServiceModel user = this.userService.findById(sendMessageBindingModel.getMessageTo());
            modelAndView.addObject("user", user);
            return super.view("user/send-message", modelAndView);
        }

        UserServiceModel messageFrom = this.userService
                .findUserByUsername(principal.getName());
        this.messageService.sendMessage(messageFrom, id, sendMessageBindingModel.getMessage(), sendMessageBindingModel.getTitle());
        return redirect("/");

    }
}
