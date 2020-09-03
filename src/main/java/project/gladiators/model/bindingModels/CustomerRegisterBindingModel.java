package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CustomerRegisterBindingModel {
    @NotNull(message = "Please add your first name")
    private String firstName;
    @NotNull(message = "Please add your last name")
    private String lastName;
    @NotNull(message = "Please add your gender")
    private String gender;
    @NotNull(message = "Please add your age")
    private int age;
    @NotNull(message = "Please add your weight")
    private double weight;
    @NotNull(message = "Please аdd your height")
    private double height;
    @NotNull(message = "You need to add your BMI data, please do this with our BMI calculator")
    private double BMI;
    private MultipartFile imageUrl;
}
