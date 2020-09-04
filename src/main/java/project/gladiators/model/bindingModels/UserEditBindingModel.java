package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserEditBindingModel {

    private String id;
    private String username;
    @Length(min = 1, message = "Please enter valid name!")
    private String firstName;
    @Length(min = 1, message = "Please enter valid name!")
    private String lastName;
    private String oldPassword;
    private String password;
    private String confirmPassword;
    private String email;
}
