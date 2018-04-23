package edu.org.auth;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import by.i4t.helper.UserRole;
import by.i4t.objects.User;

public class SecurityManager {
    public static User getUser() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return (User) req.getSession().getAttribute("userInfo");
    }

    public static Boolean isUserAuth() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return "OK".equalsIgnoreCase((String) req.getSession().getAttribute("auth_result"));
    }

    public static Boolean isAdmin() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Object obj = req.getSession().getAttribute("userInfo");
        if (obj != null)
            return UserRole.ADMIN.getCode().equals(((User) req.getSession().getAttribute("userInfo")).getRole());
        else
            return false;
    }

    public static void sessionTerminate() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        req.getSession().setAttribute("session_timeout", true);
    }

    public static Boolean isSessionTimeout() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (req.getSession().getAttribute("session_timeout") == null)
            return false;
        else
            return (Boolean) req.getSession().getAttribute("session_timeout");
    }

    public static void resetSessionTimeout() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        req.getSession().removeAttribute("session_timeout");
    }
}
