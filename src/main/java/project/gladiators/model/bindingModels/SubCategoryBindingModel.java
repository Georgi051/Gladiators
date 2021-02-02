package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.Category;
import project.gladiators.service.serviceModels.CategoryServiceModel;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;

@Getter
@Setter
@NoArgsConstructor
public class SubCategoryBindingModel {

    private String name;
    private Category category;

}
