package edu.org.spring.jsfbridge;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.web.jsf.FacesContextUtils;

@ManagedBean(name = "serviceFactory")
@SessionScoped
public class ISDOServiceFactory {
    public Object getService(String name) {
        return FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance()).getBean(name);
    }
}
