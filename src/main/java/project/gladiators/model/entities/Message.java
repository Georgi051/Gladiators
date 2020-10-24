package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message extends BaseEntity{

    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String text;

    @Column(name = "time_sent")
    private LocalDateTime timeSent;

    @Column(name = "is_unread")
    private boolean isUnread;

    @ManyToOne
    private User messageTo;

    @ManyToOne
    private User messageFrom;
}
