package edu.org.models.lineitems;

import lombok.Data;

import java.util.Date;

@Data
public class BadExportedEduDocLI extends EduDocLineItem {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String error;
    private Date exportDate;

}
