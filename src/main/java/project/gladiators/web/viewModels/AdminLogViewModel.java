package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminLogViewModel extends BaseViewModel {
    private String action;
    private String description;
    private LocalDateTime madeOn;
    private UserViewModel admin;
    private UserViewModel user;
}
