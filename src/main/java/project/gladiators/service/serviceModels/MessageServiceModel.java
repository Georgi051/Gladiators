package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageServiceModel extends BaseServiceModel{

    private String title;

    private String text;

    private UserServiceModel messageFrom;

    private LocalDateTime timeSent;

    private boolean isUnread;
}
