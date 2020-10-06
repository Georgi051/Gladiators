package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainerViewModel extends BaseViewModel {

    private int yearsOfExperience;
    private String description;
    private String userFirstName;
    private String userLastName;
    private String userImageUrl;


}
