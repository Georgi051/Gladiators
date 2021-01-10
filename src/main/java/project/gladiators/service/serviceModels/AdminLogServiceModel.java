package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminLogServiceModel extends BaseServiceModel {
    private String action;
    private String description;
    private LocalDateTime madeOn;
    private UserServiceModel admin;
    private UserServiceModel user;
}
