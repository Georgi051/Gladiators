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
public class ExerciseAddBindingModel {

    private String name;
    private String description;
    @NotEmpty(message = "You must select one or more muscle.")
    private Set<Muscle> muscles;
}
