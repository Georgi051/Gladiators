package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DeliveryServiceModel extends BaseServiceModel{

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
