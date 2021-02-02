package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SubCategoryServiceModel  extends BaseServiceModel{

    private String name;
    private boolean isEmpty;
    private Set<ProductServiceModel> products;
    private CategoryServiceModel category;
}
