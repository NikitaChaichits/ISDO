package edu.org.models;

import by.i4t.helper.EduDocsStatus;
import edu.org.models.lineitems.EduDocLineItem;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import edu.org.models.lineitems.NotificationDataLineItem;
import edu.org.utils.ColumnModel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EduDocsDataViewModel
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Boolean isAdmin = false;
    private Boolean isAuthOK = true;
    /**
     * FILTER DATA SECTION.
     */
    private String firstName;
    private String secondName;
    private String patronimyc;
    private String personalIDNumber;
    private SimpleStringValueLineItem selectedEduOrg;
    private String selectedEduLevel;
    private String docSeria;
    private String docNumber;
    private String specName;
    private String selectedSpeciality;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String selectedEduDocsDate;
    private String docRegNumber;
    private EduDocsStatus selectedEduDocsStatus;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String selectedStartDate;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String selectedStopDate;
    private SimpleStringValueLineItem selectedSpec;

    //	private List<SimpleIntValueLineItem> eduDocsStatusList = new ArrayList<SimpleIntValueLineItem>();
    //  private SimpleStringValueLineItem selectedEduLevel;

//    <!--<h:outputLabel value="Уровень образования"/>-->
//    <!--<p:selectOneMenu value="#{eduDocsDataCtrl.viewModel.selectedEduLevel}" style="width:300px;">-->
//    <!--<f:selectItem itemLabel="-&#45;&#45;&#45;&#45; Все уровни -&#45;&#45;&#45;&#45;" itemValue="#{null}"/>-->
//    <!--<f:selectItems value="#{eduDocsDataCtrl.eduLevels}" var="level" itemLabel="#{level.name}"/>-->
//    <!--</p:selectOneMenu>-->


    /**
     * DATA TABLE SECTION.
     */
    private List<ColumnModel> dataTableColumnList = new ArrayList<ColumnModel>();
    private EduDocLineItem selectedDocLine;


    public Integer getSelectedStartDate() {
        if (selectedStartDate != null)
            return Integer.valueOf(selectedStartDate);
        else
            return null;
    }

    public void setSelectedStartDate(Integer selectedStartDate) {
        if (selectedStartDate != 0)
            this.selectedStartDate = String.valueOf(selectedStartDate);
        else
            this.selectedStartDate = null;
    }

    public Integer getSelectedStopDate() {
        if (selectedStopDate != null)
            return Integer.valueOf(selectedStopDate);
        else
            return null;
    }

    public void setSelectedStopDate(Integer selectedStopDate) {
        if (selectedStopDate != 0)
            this.selectedStopDate = String.valueOf(selectedStopDate);
        else
            this.selectedStopDate = null;
    }

    public Integer getSelectedEduDocsDate() {
        if (selectedEduDocsDate != null)
            return Integer.valueOf(selectedEduDocsDate);
        else
            return null;
    }

    public void setSelectedEduDocsDate(Integer selectedEduDocsDate) {
        if (selectedEduDocsDate != null && selectedEduDocsDate != 0)
            this.selectedEduDocsDate = String.valueOf(selectedEduDocsDate);
        else
            this.selectedEduDocsDate = null;
    }
}
