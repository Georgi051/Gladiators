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


    private double weight;

    private double height;
  /*  @Positive(message = "Please enter valid BMI")
    private double BMI;*/
    private double chest;
    private double waist;
    private double biceps;
    private double thigh;
}
