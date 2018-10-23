package edu.org.models;

import edu.org.models.lineitems.AdministratorStatisticsLineItem;
import edu.org.models.lineitems.SimpleIntValueLineItem;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.PieChartModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class EduDocsStatViewModel {
    private Integer highEduDocsCount;
    private PieChartModel highEduDocsChartModel;
    private List<SimpleIntValueLineItem> highEduDocsYearList = new ArrayList<SimpleIntValueLineItem>();
    private List<SimpleIntValueLineItem> eduDocsStatByLevelList = new ArrayList<SimpleIntValueLineItem>();
    private List<AdministratorStatisticsLineItem> highEduDocsStatList = new ArrayList<AdministratorStatisticsLineItem>();
    private Integer highEduDocsSelectedYear;

    private PieChartModel middleSpecEduDocsChartModel;
    private PieChartModel profTechEduDocsChartModel;
    private PieChartModel middleEduDocsChartModel;

    private Date eduStartDate;
    private Date eduStopDate;
    private SimpleStringValueLineItem selectedEduOrg;

}
