package project.gladiators.service;

import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.MessageServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.util.List;

public interface MessageService {

    List<MessageServiceModel> findAllByUserId(String id);

    MessageServiceModel findById(String id);

    void changeStatusToRead(MessageServiceModel messageServiceModel);

    void deleteMessageById(String id);

    void sendMessage(UserServiceModel messageFrom, String id, String message, String title);
}
