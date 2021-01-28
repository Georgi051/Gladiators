package project.gladiators.service;

import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.MessageServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.web.viewModels.MessageViewModel;

import java.util.List;

public interface MessageService {

    MessageServiceModel findById(String id);

    void changeStatusToRead(MessageServiceModel messageServiceModel);

    void deleteMessageById(String id);

    void sendMessage(UserServiceModel messageFrom, String id, String message, String title);

    MessageViewModel getMessageInfo(String id);

    List<MessageViewModel> getSortedMessagesByUserId(String id);
}
