package project.gladiators.validators.customer;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.SendMessageBindingModel;

import static project.gladiators.validators.customer.CustomerConstants.MASSAGE_MUST_BE_MORE_THAN_30;
import static project.gladiators.validators.customer.CustomerConstants.TITLE_MUST_BE_BETWEEN_THREE_AND_THIRTY;

@Validator
public class SendMessageValidator implements org.springframework.validation.Validator{

    @Override
    public boolean supports(Class<?> aClass) {
        return SendMessageBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SendMessageBindingModel sendMessageBindingModel = (SendMessageBindingModel) o;

        if (sendMessageBindingModel.getTitle().length() < 3 ||
        sendMessageBindingModel.getTitle().length() > 30){
            errors.rejectValue("title",TITLE_MUST_BE_BETWEEN_THREE_AND_THIRTY,
                    TITLE_MUST_BE_BETWEEN_THREE_AND_THIRTY);
        }

        if (sendMessageBindingModel.getMessage().length() < 30){
            errors.rejectValue("message",MASSAGE_MUST_BE_MORE_THAN_30,
                    MASSAGE_MUST_BE_MORE_THAN_30);
        }

    }
}
