package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserViewModel extends BaseViewModel{

    private String username;
    private String imageUrl;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredOn;
    private int age;
    private String gender;
    private String email;
}
