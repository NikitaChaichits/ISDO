package edu.org.models.lineitems;

import by.i4t.objects.ImportedFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportedFileLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID dbId;

    private Date uploadDate;

    private String importedFileName;

    private Integer successRowCount;

    private String errorsFileName;

    private Integer errorsCount;

    private String status = "в обработке";

    private String exception;

    public ImportedFileLineItem(ImportedFile file) {
        try {

            this.uploadDate = file.getUploadDate();
            this.dbId = file.getID();
            this.importedFileName = file.getImportedFileName();
            if (file.getErrorCount() != null && file.getErrorCount() != 0) {
                this.errorsFileName = "errors_";
                String[] arr = file.getImportedFileName().split("\\.");
                if (arr[arr.length - 1].equalsIgnoreCase("xml")) {
                    for (int i = 0; i < arr.length - 1; i++)
                        this.errorsFileName += arr[i];
                    this.errorsFileName += ".xls";
                } else
                    this.errorsFileName += file.getImportedFileName();
            }
            this.successRowCount = file.getSuccessRowCount();
            this.errorsCount = file.getErrorCount();
            switch (file.getStatus()) {
                case 0:
                    this.status = "в обработке";
                    break;
                case 1:
                    this.status = "обработан";
                    break;
                case 2:
                    this.status = "ошибка формата";
                    this.exception = file.getExceptionInfo().getMessage();
                    break;
                default:
                    this.status = "ошибка";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
