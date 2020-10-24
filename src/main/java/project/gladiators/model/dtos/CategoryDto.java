package project.gladiators.model.dtos;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.SubCategory;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    @Expose
    private String name;
    @Expose
    private Set<SubCategory> subCategories;
}
