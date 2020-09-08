package project.gladiators.model.dtos;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExerciseDto {
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private String imageUrl;
}
