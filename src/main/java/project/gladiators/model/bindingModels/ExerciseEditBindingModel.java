package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.entities.Muscle;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseEditBindingModel {
    @Size(min = 3, message = "Exercise name must be at least 3 characters")
    private String name;
    @Size(min = 30, max = 1000, message = "The description must be between 30 and 1000 characters!")
    private String description;
    @NotNull(message = "Please add exercise picture.")
    private MultipartFile imageUrl;
    @NotEmpty(message = "You must select muscle one or more.")
    private Set<Muscle> muscles;
}
