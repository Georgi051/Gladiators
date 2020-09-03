package project.gladiators.model.bindingModels;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterBindingModel {

    private String username;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@~$%^&*-]).{8,}\\S+$"
    ,message = "Password should be with one upper case, one lower case, one digit[0-9], \n" +
            "   one special character[#?!@$%~^&*-] and the minimum length should be 8.")
    private String password;
    private String confirmPassword;
    @Email(message = "Your email is invalid")
    private String email;

}
