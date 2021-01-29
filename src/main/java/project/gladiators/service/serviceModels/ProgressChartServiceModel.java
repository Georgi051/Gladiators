package project.gladiators.service.serviceModels;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProgressChartServiceModel extends BaseServiceModel{

    private double weight;
    private double height;
    private double chest;
    private double waist;
    private double biceps;
    private double thigh;
}
