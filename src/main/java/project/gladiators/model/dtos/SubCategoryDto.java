package project.gladiators.model.dtos;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.Muscle;
import project.gladiators.model.entities.SubCategory;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SubCategoryDto {
    @Expose
    private String name;
}
