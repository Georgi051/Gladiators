package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryBindingModel {
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String neighborhood;
    private String city;
    private int zip;
    private String phoneNumber;
    private String payMethod;
}
