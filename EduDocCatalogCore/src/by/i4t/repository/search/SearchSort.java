package by.i4t.repository.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Sort;

/**
 * Created by pavel.luchenok on 19.10.2016.
 */
@Data
@AllArgsConstructor
public class SearchSort {
    private Sort.Direction direction;
    private String property;
}
