package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.Exercise;
import project.gladiators.model.entities.SubCategory;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryBindingModel {
    @Size(min = 4,max = 20,message = "The category must be between 4 and 20 characters!")
    private String name;
}
