package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewServiceModel extends BaseServiceModel {
    private String description;
    private int stars;
    private LocalDateTime reviewDate;
    private UserServiceModel user;
}
