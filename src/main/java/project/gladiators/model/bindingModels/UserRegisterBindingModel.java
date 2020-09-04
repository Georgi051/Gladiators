package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterBindingModel {

    @Length(min = 1,message = "Please add your username")
    private String username;
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@~$%^&*-]).{7,}\\S+$"
    ,message = "Password should be with one upper case, one lower case, one digit[0-9], \n" +
            "   one special character[#?!@$%~^&*-] and the minimum length should be more than 7.")
    private String password;
    private String confirmPassword;
    @Email(message = "Your email is invalid")
    private String email;

}
