package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.ProgressChart;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CustomerServiceModel extends BaseServiceModel{

    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private double weight;
    private double height;
    private double BMI;
    private ProgressChart progressChart;
    private String imageUrl;
}
