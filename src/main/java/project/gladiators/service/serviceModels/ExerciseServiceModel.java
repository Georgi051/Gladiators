package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseServiceModel extends BaseServiceModel {
    private String name;
    private String description;
    private List<MuscleServiceModel> muscles;
}
