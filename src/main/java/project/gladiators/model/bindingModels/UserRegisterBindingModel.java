package project.gladiators.model.bindingModels;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterBindingModel {

    private String username;
    private String password;
    private String confirmPassword;
    @Email
    private String email;

}
