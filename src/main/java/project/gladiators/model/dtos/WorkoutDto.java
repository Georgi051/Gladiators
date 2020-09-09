package project.gladiators.model.dtos;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WorkoutDto {

    @Expose
    private String name;

    @Expose
    private int duration;
}
