package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class ProgressChartEditBindingModel {

    @Min(value = 30, message = "Please enter valid weight")
    private double weight;
    @Min(value = 57, message = "Please enter valid height")
    private double height;
    @Positive(message = "Please enter valid BMI")
    private double BMI;
    @Positive(message = "Please enter valid chest")
    private double chest;
    @Positive(message = "Please enter valid waist")
    private double waist;
    @Positive(message = "Please enter valid biceps")
    private double biceps;
    @Positive(message = "Please enter valid thigh")
    private double thigh;
}
