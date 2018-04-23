package edu.org.models.lineitems;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdministratorStatisticsLineItem {
    private String eduOrgName;
    private Integer correctDocsCount;
    private Integer incorrectDocsCount;
}
