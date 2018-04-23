package by.i4t.repository.search;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by pavel.luchenok on 19.10.2016.
 */

public class SearchPageRequest extends AbstractPageRequest {
    //    private int page;
//    private int size;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private SearchSort sort;

    public SearchPageRequest(int page, int size, SearchSort direction) {
        super(page, size);
        this.sort = direction;
    }
    public SearchPageRequest(int page, int size, Sort.Direction direction, String property) {
        this(page, size, new SearchSort(direction, property));
    }

//    public SearchPageRequest(int page, int size) {
//        this(page, size, null);
//    }

//    @Override
//    public int getPageNumber() {
//        return page;
//    }
//
//    @Override
//    public int getPageSize() {
//        return size;
//    }
//
//    @Override
//    public int getOffset() {
//        return page * size;
//    }

    @Override
    public Sort getSort() {
        if (sort == null)
            return null;
        return new Sort(sort.getDirection(), sort.getProperty());
    }

    @Override
    public Pageable next() {
        return new SearchPageRequest(getPageNumber() + 1, getPageSize(), sort);
    }

    @Override
    public Pageable previous() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return getPageNumber() == 0 ? this : new SearchPageRequest(getPageNumber() - 1, getPageSize(), sort);
    }

    @Override
    public Pageable first() {
        return new SearchPageRequest(0, getPageSize(), sort);
    }

    @Override
    public boolean hasPrevious() {
        return getPageNumber() > 0;
    }
}
