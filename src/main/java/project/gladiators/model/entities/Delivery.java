package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "delivery")
public class Delivery extends BaseEntity{

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column
    private String address;
    @Column
    private String email;
    @Column
    private String neighborhood;
    @Column
    private String city;
    @Column
    private int zip;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "pay_method")
    private String payMethod;
}
