package by.i4t.repository.search;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by pavel.luchenok on 18.10.2016.
 */
@Data
@AllArgsConstructor
public class SearchFilter {
    private String combine;
    private List<SearchRestriction> restrictions;
}
