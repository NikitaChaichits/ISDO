package by.i4t.repository.search;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by pavel.luchenok on 17.10.2016.
 */
@Data
@AllArgsConstructor
public class SearchRestriction {
    private String key;
    private String operation;
    private Object value;
    private Object from;
    private Object to;

}
