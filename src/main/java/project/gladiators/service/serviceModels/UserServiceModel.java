package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.enums.Gender;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel extends BaseServiceModel{
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredOn;
    private String imageUrl;
    private Gender gender;
    private String password;
    private String email;
    private int age;
    private Set<RoleServiceModel> authorities;

}
