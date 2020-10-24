package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageViewModel extends BaseViewModel{

      private String title;
      private String messageFrom;
      private String imageOfSender;
      private String idOfSender;
      private String message;
      private boolean unread;
      private LocalDateTime timeSent;
}
