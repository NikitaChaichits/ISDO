package edu.org.validators;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ManagedBean
@RequestScoped
public class EduStopDateValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        if (value == null)
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Не указана дата окончания обучения", ""));
        Date eduStopDate = (Date) value;
        UIInput eduStartDateInput = (UIInput) context.getViewRoot().findComponent("edu_doc_dlg_form:edu_start_date");

        Date eduStartDate = (Date) eduStartDateInput.getValue();
        boolean startError = false;
        if (eduStartDate.after(eduStopDate)) {
            eduStartDateInput.setValid(false);
            startError = true;
        }

        boolean issueError = false;
        UIInput docIssueDateInput = (UIInput) context.getViewRoot().findComponent("edu_doc_dlg_form:doc_issue_date");
        docIssueDateInput.validate(context); //should be validated before eduStopDate
        Date docIssueDate = (Date) docIssueDateInput.getValue();
        if (docIssueDate != null) {
            if (docIssueDate.before(eduStartDate)) {
                docIssueDateInput.setValid(false);
                issueError = true;
            }
        }

        if (startError && issueError) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Неверный промежуток начала и окончания обучения", ""));
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Неверный промежуток окончания обучения и выдачи документа", ""));
        }
        if (startError) {
            docIssueDateInput.setValid(true);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Неверный промежуток начала и окончания обучения", ""));
        }
        if (issueError) {
            eduStartDateInput.setValid(true);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Неверный промежуток окончания обучения и выдачи документа", ""));
        }
    }

}
