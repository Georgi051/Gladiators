package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRegisterBindingModel {

    @Size(min = 8,max = 80,message = "The title must be between 8 and 40 characters!")
    private String title;

    @Size(min = 50, max = 2000, message = "The description must be between 50 and 2000 characters!")
    private String description;

    @NotNull
    private MultipartFile image;
}
