package edu.org.validators;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import edu.org.models.EduDocDetailsDialogViewModel;

@ManagedBean
@RequestScoped
public class EduOrgValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        // TODO Auto-generated method stub
        if (value == null)
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Не указано учреждение образования", ""));
    }

}
