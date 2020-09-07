package project.gladiators.service.serviceModels;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.enums.Gender;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

public class UserServiceModel extends BaseServiceModel{
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredOn;
    private String imageUrl;
    private Gender gender;
    private String password;
    private String email;
    private LocalDate dateOfBirth;
    private boolean isTrainer;
    private Set<RoleServiceModel> authorities;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserServiceModel)) return false;
        if (!super.equals(o)) return false;
        UserServiceModel that = (UserServiceModel) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(registeredOn, that.registeredOn) &&
                Objects.equals(imageUrl, that.imageUrl) &&
                gender == that.gender &&
                Objects.equals(password, that.password) &&
                Objects.equals(email, that.email) &&
                Objects.equals(dateOfBirth, that.dateOfBirth) &&
                Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, firstName, lastName, registeredOn, imageUrl, gender, password, email, dateOfBirth, authorities);
    }
}
