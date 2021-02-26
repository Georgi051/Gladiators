package project.gladiators.integrational.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.model.entities.Message;
import project.gladiators.model.entities.User;
import project.gladiators.repository.MessageRepository;
import project.gladiators.repository.UserRepository;
import project.gladiators.service.MessageService;
import project.gladiators.service.serviceModels.MessageServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.web.viewModels.MessageViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MessageServiceImplTest {

    Message message;
    User messageTo;
    User messageFrom;
    List<User> users;
    Message message2;
    List<Message> messages;

    @MockBean
    MessageRepository messageRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    ModelMapper modelMapper;

    @BeforeEach
    public void messageInit(){
        message = new Message();
        message.setId("1");
        message.setTitle("Test");
        messageTo = new User();
        messageTo.setId("1");
        messageFrom = new User();
        messageFrom.setId("2");
        message.setMessageTo(messageTo);
        message.setMessageFrom(messageFrom);
        message.setTimeSent(LocalDateTime.now().minusMinutes(5));
        message.setUnread(true);
        message.setText("Test");

        users = new ArrayList<>();
        users.add(messageTo);
        users.add(messageFrom);
        message2 = new Message();
        message2.setId("2");
        message2.setMessageFrom(messageFrom);
        message2.setMessageTo(messageTo);
        message2.setTimeSent(LocalDateTime.now());
        messages = new ArrayList<>();
        messages.add(message);
        messages.add(message2);
    }

    @Test
    public void findById_shouldReturnCorrectMessage(){
        when(messageRepository.findById("1"))
                .thenReturn(Optional.of(message));
        MessageServiceModel messageServiceModel = messageService.findById("1");

        assertEquals(message.getTitle(), messageServiceModel.getTitle());

    }

    @Test
    public void findById_shouldThrowExceptionIfNoMessageFound(){
        Message message = new Message();
        message.setId("1");

        assertThrows(IllegalArgumentException.class, () ->
                messageService.findById(message.getId()), message.getId());
    }

    @Test
    public void changeStatusToRead_shouldChangeMessageStatus(){
        when(messageRepository.findById("1"))
                .thenReturn(Optional.of(message));

        MessageServiceModel messageServiceModel = modelMapper
                .map(message, MessageServiceModel.class);
        messageService.changeStatusToRead(messageServiceModel);

        assertFalse(message.isUnread());

    }

    @Test
    public void deleteMessageById_shouldDeleteMessage(){
        when(messageRepository.findById("1"))
                .thenReturn(Optional.of(message));

        messageService.deleteMessageById("1");
        verify(messageRepository)
                .delete(any());
    }

    @Test
    public void getSortedMessagesByUserId_shouldReturnSortedMessages(){

        when(userRepository.findById("1"))
                .thenReturn(Optional.of(messageTo));
        message.setMessageTo(messageTo);
        message.setMessageFrom(messageFrom);
        message.setTimeSent(LocalDateTime.now().minusMinutes(5));
        when(messageRepository.findAllByMessageTo(messageTo))
                .thenReturn(messages);
        List<MessageViewModel> messageViewModels = messageService.getSortedMessagesByUserId("1");

        assertEquals("2", messageViewModels.get(0).getId());

    }

    @Test
    public void getMessageInfo_shouldReturnMessageInfo(){
        when(messageRepository.findById("1"))
                .thenReturn(Optional.of(message));
        MessageViewModel messageViewModel = messageService.getMessageInfo("1", null);
        assertEquals("Test", messageViewModel.getTitle());

    }

    @Test
    public void sendMessage_shouldSendMessageToGivenUser(){
        UserServiceModel userServiceModel = modelMapper
                .map(messageFrom, UserServiceModel.class);
        when(userRepository.findById("1"))
                .thenReturn(Optional.of(messageFrom));
        when(userRepository.findById("2"))
                .thenReturn(Optional.of(messageTo));
        messageService.sendMessage(userServiceModel, "1", "Some text", "Some Title");

        verify(messageRepository)
                .saveAndFlush(any(Message.class));
    }
}
