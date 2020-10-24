package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class SendMessageBindingModel {

    private String messageTo;

    @Length(min = 3, max = 30, message = "Your title should have length of min 3 and max 30 symbols")
    private String title;

    @Length(min = 30, message = "Your message should be longer than 30 symbols")
    private String message;
}
