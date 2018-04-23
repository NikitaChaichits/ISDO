package edu.org.controllers;

import by.i4t.helper.UserStatusEnum;
import by.i4t.objects.User;
import edu.org.models.EmptyModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ManagedBean(name = "debugCtrl")
@SessionScoped
public class DebugCtrl extends EduDocCommonCtrl<EmptyModel> {

    private String certId;

    @PostConstruct
    public void init() {
        super.init();

    }

    public void switchUser() {
        User user = getRepositoryService().getUserRepository().findByCertificatId(certId);
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        session.invalidate();
        session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        if (user != null
                && !UserStatusEnum.BLOCKED.getCode().equals(user.getStatus())) {
            session.setAttribute("userInfo", user);
            session.setAttribute("auth_result", "OK");
            System.out.println("User switched to " + user.getName());
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext().redirect("/EduOrg");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }
}