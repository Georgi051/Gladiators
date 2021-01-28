package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.Message;
import project.gladiators.model.entities.User;
import project.gladiators.repository.MessageRepository;
import project.gladiators.repository.UserRepository;
import project.gladiators.service.MessageService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.MessageServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.web.viewModels.MessageViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public MessageServiceImpl(UserService userService, MessageRepository messageRepository, UserRepository userRepository, ModelMapper modelMapper) {
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
    public MessageViewModel getMessageInfo(String id) {

        MessageServiceModel messageServiceModel = this.findById(id);

        this.changeStatusToRead(messageServiceModel);

        MessageViewModel message = this.modelMapper.map
                (messageServiceModel, MessageViewModel.class);
        message.setMessageFrom(String.format
                ("%s %s", messageServiceModel.getMessageFrom().getFirstName(),
                        messageServiceModel.getMessageFrom().getLastName()));
        message.setMessage(messageServiceModel.getText());
        message.setImageOfSender(messageServiceModel.getMessageFrom().getImageUrl());
        message.setIdOfSender(messageServiceModel.getMessageFrom().getId());

        return message;


    }

    @Override
    public List<MessageViewModel> getSortedMessagesByUserId(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        List<Message> messages = this.messageRepository.
                findAllByMessageTo(user);
        List<MessageViewModel> messageViewModels = new ArrayList<>();
        messages
                .forEach(message -> {
                    MessageViewModel messageViewModel = new MessageViewModel();
                    messageViewModel.setTimeSent(message.getTimeSent());
                    messageViewModel.setIdOfSender(message.getMessageFrom().getId());
                    messageViewModel.setTitle(message.getTitle());
                    messageViewModel.setId(message.getId());
                    messageViewModel.setMessageFrom(String.format
                            ("%s %s", message.getMessageFrom().getFirstName(),
                                    message.getMessageFrom().getLastName()));
                    messageViewModels.add(messageViewModel);
                });

        List<MessageViewModel> sortedMessages = messageViewModels.stream().sorted(Comparator.comparing(MessageViewModel::getTimeSent).reversed())
                .collect(Collectors.toList());

        return sortedMessages;

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
