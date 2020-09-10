package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.entities.Muscle;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseEditBindingModel {
    private String name;
    private String description;
    @NotNull
    private MultipartFile imageUrl;
    @NotEmpty(message = "You must select muscle one or more")
    private Set<Muscle> muscles;
}
