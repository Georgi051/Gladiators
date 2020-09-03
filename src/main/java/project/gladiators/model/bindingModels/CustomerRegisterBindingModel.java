package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
public class CustomerRegisterBindingModel {
    @Length(min = 1,message = "Please add your first name")
    private String firstName;
    @Length(min = 1,message = "Please add your last name")
    private String lastName;
    @Length(min = 1,message = "Please add your gender")
    private String gender;
    @Length(min = 1,message = "Please add your age")
    private int age;
    @Length(min = 1,message = "Please add your weight")
    @Min(value = 30 ,message = "Your weight is incorrect")
    private double weight;
    @Length(min = 1,message = "Please аdd your height")
    @Min(value = 57,message = "Your height is incorrect")
    private double height;
    @Length(min = 1,message = "You need to add your BMI data, please do this with our BMI calculator")
    private double BMI;
    private MultipartFile imageUrl;
}
