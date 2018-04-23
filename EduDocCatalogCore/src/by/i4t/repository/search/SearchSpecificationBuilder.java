package by.i4t.repository.search;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.List;

/**
 * Created by pavel.luchenok on 17.10.2016.
 */
public class SearchSpecificationBuilder<T> {
    private SearchFilter searchFilter;

    public SearchSpecificationBuilder(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
    }

    public Specification<T> build() {
        if (searchFilter == null || searchFilter.getRestrictions() == null || searchFilter.getRestrictions().isEmpty())
            return null;
        List<SearchRestriction> criteria = searchFilter.getRestrictions();
        String combine = searchFilter.getCombine();
        Specification result = new SearchSpecification<>(criteria.get(0));
        if (combine != null)
            if (combine.equalsIgnoreCase("or"))
                for (int i = 1; i < criteria.size(); i++)
                    result = Specifications.where(result).or(new SearchSpecification(criteria.get(i)));
            else if (combine.equalsIgnoreCase("and"))
                for (int i = 1; i < criteria.size(); i++)
                    result = Specifications.where(result).and(new SearchSpecification(criteria.get(i)));
        return result;
    }
}
