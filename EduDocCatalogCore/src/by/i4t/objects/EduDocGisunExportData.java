package by.i4t.objects;

import lombok.Data;

@Data
public class EduDocGisunExportData {
    private String personalID;
    private String eduDocNumber;
    private String eduDocSeries;
    private String eduDocDateOfIssue;
    private String eduDocTypeCode;
    private String eduOrgCode;
    private String educationBeginData;
    private String educationEndData;
    private String educationRecordNumber;
    private String specialtyCode;

    public Boolean check() {
        return !(personalID == null || eduDocNumber == null || eduDocSeries == null ||
                eduDocDateOfIssue == null || eduDocTypeCode == null || eduOrgCode == null || educationBeginData == null ||
                educationEndData == null || educationRecordNumber == null || specialtyCode == null);
    }
}
