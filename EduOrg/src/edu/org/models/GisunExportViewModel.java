package edu.org.models;

import by.i4t.objects.ExceptionInfo;
import edu.org.models.lineitems.BadExportedEduDocLI;
import edu.org.models.lineitems.SimpleIntValueLineItem;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import edu.org.utils.ColumnModel;
import lombok.Data;
import org.primefaces.model.chart.PieChartModel;

import java.io.Serializable;
import java.util.List;

@Data
public class GisunExportViewModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4087574092833932444L;

    private String serverStatus;
    // Monitoring info of the export process
    private PieChartModel exportDataChartModel;
    private PieChartModel exportErrorChartModel;
    private String exportStatLastUpdate;
    private StringBuffer exportLog;
    private List<ColumnModel> exportExceptionTableColumns;
    private List<ExceptionInfo> exportExceptionTableData;

    // Bad exported data info
    private List<ColumnModel> badExportedEduDocsTableColumnList;
    private List<BadExportedEduDocLI> badExportedEduDocsLIList;
    private List<SimpleIntValueLineItem> errorsByEduOrgList;
    private List<SimpleStringValueLineItem> errorTypes;
    //private List<SimpleIntValueLineItem> gisunInfoBySelectedErrorTypeList;
    //private String errorCodeForCollection;
    private String selectedErrorType;
    private String exportFileName;

    private Boolean isExportServiceOn;
    private Boolean isExportServiceOK;
}
