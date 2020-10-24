package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.Customer;
import project.gladiators.model.entities.Message;
import project.gladiators.model.entities.Trainer;
import project.gladiators.model.entities.User;
import project.gladiators.repository.CustomerRepository;
import project.gladiators.repository.MessageRepository;
import project.gladiators.repository.TrainerRepository;
import project.gladiators.repository.UserRepository;
import project.gladiators.service.MessageService;
import project.gladiators.service.TrainerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.MessageServiceModel;
import project.gladiators.service.serviceModels.TrainerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final CustomerRepository customerRepository;
    private final TrainerRepository trainerRepository;
    private final TrainerService trainerService;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public MessageServiceImpl(CustomerRepository customerRepository, TrainerRepository trainerRepository, TrainerService trainerService, UserService userService, MessageRepository messageRepository, UserRepository userRepository, ModelMapper modelMapper) {

        this.customerRepository = customerRepository;
        this.trainerRepository = trainerRepository;
        this.trainerService = trainerService;
        this.userService = userService;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void sendMessage(UserServiceModel messageFrom, String id, String message, String title) {

        UserServiceModel messageTo = this.userService
                .findById(id);
        Message messageText = new Message();
        messageText.setText(message);
        messageText.setTimeSent(LocalDateTime.now());
        messageText.setUnread(true);
        messageText.setTitle(title);

        User userTo = this.userRepository.findById(messageTo.getId()).orElse(null);
        User userFrom = this.userRepository.findById(messageFrom.getId()).orElse(null);
        if (userTo != null && userFrom != null) {
            messageText.setMessageFrom(userFrom);
            messageText.setMessageTo(userTo);

            this.userRepository.save(userFrom);
            this.userRepository.save(userTo);
            this.messageRepository.saveAndFlush(messageText);
        }
    }

    @Override
    public List<MessageServiceModel> findAllByUserId(String id) {

        List<MessageServiceModel> messages = new ArrayList<>();
        User user = this.userRepository.findById(id).orElse(null);
        this.messageRepository
                .findAllByMessageTo(user).stream()
                .forEach(message -> {
                    MessageServiceModel messageServiceModel = this.modelMapper
                            .map(message, MessageServiceModel.class);
                    messageServiceModel.setMessageFrom(this.modelMapper
                            .map(message.getMessageFrom(), UserServiceModel.class));
                    messages.add(messageServiceModel);
                });
        return messages;
    }

    @Override
    public MessageServiceModel findById(String id) {

        Message message = this.messageRepository.findById(id).orElse(null);

        return this.modelMapper
                .map(message, MessageServiceModel.class);
    }

    @Override
    public void changeStatusToRead(MessageServiceModel messageServiceModel) {

        Message message = this.messageRepository.findById(messageServiceModel.getId())
                .orElse(null);

        if (message != null) {
            message.setUnread(false);
            messageRepository.saveAndFlush(message);
        }
    }

    @Override
    public void deleteMessageById(String id) {

        Message message = this.messageRepository.findById(id).orElse(null);

        if (message != null) {
            this.messageRepository.delete(message);
        }
    }
}
