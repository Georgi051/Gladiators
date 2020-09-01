package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerServiceModel {
    private String firstName;
    private String lastName;
    private String gender;
    private double weight;
    private double height;
    private double BMI;
    private String imageUrl;
}
