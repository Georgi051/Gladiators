package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class CustomerRegisterBindingModel {
    private String firstName;
    private String lastName;
    private String gender;
    private double weight;
    private double height;
    private double BMI;
    //TODO Ако се направи imageUrl String не гърми
//    private MultipartFile imageUrl;
}
