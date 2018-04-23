package edu.org.models.lineitems;

import by.i4t.helper.CompareObjectsUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SimpleStringValueLineItem implements Serializable {
    private String name;
    private String value;


    @Override
    public boolean equals(Object obj) {
        return CompareObjectsUtils.compareStrings(getName(), ((SimpleStringValueLineItem) obj).getName()) &&
                CompareObjectsUtils.compareStrings(getValue(), ((SimpleStringValueLineItem) obj).getValue());
    }

    @Override
    public String toString() {
        return name;
    }
}
