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
    @Min(value = 1,message = "Please add your age")
    private int age;
    @Min(value = 30 ,message = "Your weight is incorrect")
    private double weight;
    @Min(value = 57,message = "Your height is incorrect")
    private double height;
    private MultipartFile imageUrl;
}
