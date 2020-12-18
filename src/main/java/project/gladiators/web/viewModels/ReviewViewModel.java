package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewViewModel extends BaseViewModel {
    private String description;
    private int stars;
    private LocalDateTime reviewDate;
    private UserViewModel user;
}
