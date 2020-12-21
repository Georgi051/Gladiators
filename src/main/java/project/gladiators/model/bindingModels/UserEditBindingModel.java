package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class UserEditBindingModel {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String oldPassword;
    private String password;
    private String confirmPassword;
    private String email;
    private MultipartFile imageUrl;
}
