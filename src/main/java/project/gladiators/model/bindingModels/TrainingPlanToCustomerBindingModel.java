package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.web.viewModels.BaseViewModel;

@Getter
@Setter
@NoArgsConstructor
public class TrainingPlanToCustomerBindingModel extends BaseViewModel {
    private String name;
    private String id;
}
