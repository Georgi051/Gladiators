package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.ProgressChart;

@Getter
@Setter
@NoArgsConstructor
public class CustomerServiceModel extends BaseServiceModel{
    private String firstName;
    private String lastName;
    private String gender;
    private int age;
    private double weight;
    private double height;
<<<<<<< HEAD
    private double BMI;
    private ProgressChart progressChart;
=======
>>>>>>> 122c163d9240bdb34871757e91a2a5487885bd05
    private String imageUrl;
}
