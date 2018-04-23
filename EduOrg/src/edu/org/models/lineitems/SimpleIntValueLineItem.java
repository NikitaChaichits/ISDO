package edu.org.models.lineitems;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleIntValueLineItem {
    private String name;
    private Integer value;

}
