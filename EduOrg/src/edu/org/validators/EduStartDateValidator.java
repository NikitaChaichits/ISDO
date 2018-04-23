package edu.org.validators;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ManagedBean
@RequestScoped
public class EduStartDateValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        if (value == null)
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Не указана дата начала обучения", ""));
    }
}
