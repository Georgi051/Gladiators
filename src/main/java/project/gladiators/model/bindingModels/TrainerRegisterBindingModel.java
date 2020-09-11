package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TrainerRegisterBindingModel {

    @Length(min = 1, message = "Please add your first name")
    private String firstName;
    @Length(min = 1, message = "Please add your last name")
    private String lastName;
    @Length(min = 1, message = "Please add your gender")
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Please enter valid date")
    @NotNull(message = "Date cannot be null")
    private LocalDate dateOfBirth;


    private MultipartFile imageUrl;

    @PositiveOrZero(message = "Years of experience must be >=0")
    private int yearsOfExperience;


    @Size(min = 50, max = 300, message = "The description must be between 50 and 300 characters")
    private String description;

}
