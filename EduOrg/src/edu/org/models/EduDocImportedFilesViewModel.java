package edu.org.models;

import edu.org.models.lineitems.ImportedFileLineItem;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EduDocImportedFilesViewModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ImportedFileLineItem> importedFilesList = new ArrayList<ImportedFileLineItem>();

}
