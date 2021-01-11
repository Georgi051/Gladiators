package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import project.gladiators.web.viewModels.BaseViewModel;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TrainingPlanToCustomerBindingModel extends BaseViewModel {
    private String name;
    private String id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startedOn;
}
