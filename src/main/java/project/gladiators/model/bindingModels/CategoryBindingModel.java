package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CategoryBindingModel {
    @Size(min = 4,max = 20,message = "The category must be between 4 and 20 characters!")
    private String name;
}
