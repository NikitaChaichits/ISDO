package edu.org.models.lineitems;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class GisunExportInfoLI implements Serializable {
    private static final long serialVersionUID = 1L;

    private String eduName;
    private Integer errorsCount;

}
