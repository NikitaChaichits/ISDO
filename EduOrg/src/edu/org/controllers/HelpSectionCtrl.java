package edu.org.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.NodeSelectEvent;

import edu.org.models.HelpSectionViewModel;
import edu.org.models.lineitems.SimpleStringValueLineItem;

@ManagedBean(name = "helpSectionCtrl")
@SessionScoped
public class HelpSectionCtrl extends
        EduDocCommonCtrl<HelpSectionViewModel> {

    @PostConstruct
    public void init() {
        super.init();
        getViewModel().setArticleUrl("help/0-0.xhtml");
    }

    public void onNodeSelect(NodeSelectEvent event) {
        SimpleStringValueLineItem data = (SimpleStringValueLineItem) event.getTreeNode().getData();
        if (data.getValue() != null && !data.getValue().isEmpty())
            getViewModel().setArticleUrl(data.getValue());
    }
}
