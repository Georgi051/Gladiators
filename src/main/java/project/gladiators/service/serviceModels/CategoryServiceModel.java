package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.SubCategory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryServiceModel extends BaseServiceModel {
    private String name;
    private List<SubCategoryServiceModel> subCategories = new ArrayList<>();
}
