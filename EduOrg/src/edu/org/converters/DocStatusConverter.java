package edu.org.converters;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import by.i4t.helper.EduDocsStatus;

@ManagedBean(name = "docStatusConverter", eager = true)
@ApplicationScoped
public class DocStatusConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String string) {
//        try {
        if (string.equals("----- Все статусы -----"))
            return null;
        if (string != null && !string.isEmpty())
            return EduDocsStatus.valueOf(string);
//        } finally {
        return null;
//        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object != null)
            return ((EduDocsStatus) object).name();
        return null;
    }

}
