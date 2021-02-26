package project.gladiators.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.bindingModels.SendMessageBindingModel;
import project.gladiators.service.MessageService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.MessageServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.validators.customer.SendMessageValidator;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/messages")
public class MessageController extends BaseController{

    private final MessageService messageService;
    private final UserService userService;
    private final SendMessageValidator sendMessageValidator;

    public MessageController(MessageService messageService, UserService userService, SendMessageValidator sendMessageValidator) {
        this.messageService = messageService;
        this.userService = userService;
        this.sendMessageValidator = sendMessageValidator;
    }

    @GetMapping("/")
    @PageTitle("Message")
    public ModelAndView messageInfo(@RequestParam("id") String id,
                                    ModelAndView modelAndView, HttpSession httpSession){

        modelAndView.addObject("message", this.messageService.getMessageInfo(id, httpSession));
        return super.view("user/message-info", modelAndView);
    }

    @GetMapping("/delete")
    public ModelAndView deleteMessage(@RequestParam("id") String id){

        this.messageService.deleteMessageById(id);

        return redirect("/");
    }

    @GetMapping("/reply/")
    @PageTitle("Send Message")
    public ModelAndView sendMessage(@RequestParam("id") String id,
                                       ModelAndView modelAndView){

        MessageServiceModel messageServiceModel = this.messageService.findById(id);
        SendMessageBindingModel sendMessageBindingModel = new SendMessageBindingModel();
        sendMessageBindingModel.setMessageTo(messageServiceModel.getMessageFrom().getId());
        sendMessageBindingModel.setTitle(messageServiceModel.getTitle());
        modelAndView.addObject("user", messageServiceModel.getMessageFrom());
        modelAndView.addObject("sendMessageBindingModel", sendMessageBindingModel);
        return super.view("user/send-message", modelAndView);
    }

    @PostMapping("/reply/")
    @PageTitle("Send Message")
    public ModelAndView sendMessage(@RequestParam("id") String id,
                                    @Valid @ModelAttribute("sendMessageBindingModel")
                                                SendMessageBindingModel sendMessageBindingModel,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView,
                                    Principal principal){
        sendMessageValidator.validate(sendMessageBindingModel,bindingResult);

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
