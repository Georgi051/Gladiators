package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Please enter valid date")
    @NotNull(message = "Date cannot be null")
    private LocalDate dateOfBirth;
    @Min(value = 30 ,message = "Your weight is incorrect")
    private double weight;
    @Min(value = 57,message = "Your height is incorrect")
    private double height;
    private MultipartFile imageUrl;
}
