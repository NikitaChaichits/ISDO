package by.i4t.repository.search;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by pavel.luchenok on 18.10.2016.
 */
@Data
@AllArgsConstructor
public class PageAndFilter {
    private SearchPageRequest request;
    private SearchFilter filter;
}
