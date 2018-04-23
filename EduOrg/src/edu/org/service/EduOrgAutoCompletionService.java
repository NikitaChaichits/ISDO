package edu.org.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import by.i4t.objects.EduOrganization;
import edu.org.controllers.EduDocCommonCtrl;
import edu.org.models.EmptyModel;
import edu.org.models.lineitems.SimpleStringValueLineItem;

@ManagedBean(name = "eduOrgAutoCompletionService")
@SessionScoped
public class EduOrgAutoCompletionService extends EduDocCommonCtrl<EmptyModel>
        implements Converter {
    private List<SimpleStringValueLineItem> eduOrgList = new ArrayList<>();

    @PostConstruct
    public void init() {
        super.init();
        List<EduOrganization> values = getAppCache().getActualEduOrgList();
        for (EduOrganization org : values)
            eduOrgList.add(new SimpleStringValueLineItem(org.getName(), org
                    .getCode().toString()));
    }

    public List<SimpleStringValueLineItem> completeTextEduOrg(String query) {
        List<SimpleStringValueLineItem> results = new ArrayList<SimpleStringValueLineItem>();
        String regex = ".*";
        for (int i = 0; i < query.length(); i++)
            regex += "[" + Character.toUpperCase(query.charAt(i))
                    + Character.toLowerCase(query.charAt(i)) + "]";
        regex += ".*";
        for (SimpleStringValueLineItem item : this.eduOrgList)
            if (item.getName().matches(regex))
                results.add(item);
        Collections.sort(results, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((SimpleStringValueLineItem) o1)
                        .getName()
                        .trim()
                        .compareTo(
                                ((SimpleStringValueLineItem) o2).getName()
                                        .trim());
            }
        });

        results.add(0, new SimpleStringValueLineItem(
                "----- Все учреждения -----", null));
        return results;
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                for (SimpleStringValueLineItem item : eduOrgList) {
                    if (item.getValue().equals(value)
                            || item.getName().equals(value))
                        return item;
                }
                /*
				 * for( SimpleStringValueLineItem item :
				 * this.getDocDetailsViewModel().getSpecialtyList() ){
				 * if(item.getValue().equals(value)) return item; }
				 */
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Conversion Error",
                        "Not a valid item."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(object);
        } else {
            return null;
        }
    }

}
