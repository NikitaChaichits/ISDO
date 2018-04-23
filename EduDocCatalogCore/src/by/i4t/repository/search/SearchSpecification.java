package by.i4t.repository.search;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by pavel.luchenok on 17.10.2016.
 */
@AllArgsConstructor
public class SearchSpecification<T> implements Specification<T> {

    private SearchRestriction restriction;

    public static boolean isEmpty(Object s) {
        return s == null || "".equals(s);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String operation = restriction.getOperation();
        switch (operation) {
            case "between": {
                Path<Integer> path = (Path<Integer>) getExpressionByKey(root, restriction.getKey());
                Integer from = (Integer) restriction.getFrom();
                Integer to = (Integer) restriction.getTo();
                if (from != null && to == null)
                    return cb.greaterThanOrEqualTo(path, from);
                else if (from == null && to != null)
                    return cb.lessThanOrEqualTo(path, to);
                else if (from != null && to != null)
                    return cb.between(path, from, to);
            }
            case "period": {
                Expression<Date> path = (Expression<Date>) getExpressionByKey(root, restriction.getKey());
                Date from = getDate(restriction.getFrom());
                Date to = getDate(restriction.getTo());
                if (from != null && to == null)
                    return cb.greaterThanOrEqualTo(path, from);
                else if (from == null && to != null)
                    return cb.lessThanOrEqualTo(path, to);
                else if (from != null && to != null)
                    return cb.between(path, from, to);
            }
            case "startsWith": {
                if (isEmpty(restriction.getValue()))
                    break;
                return cb.like(cb.lower(getExpressionByKey(root, restriction.getKey()).as(String.class)), restriction.getValue().toString().toLowerCase() + "%");
            }
            case "endsWith": {
                if (isEmpty(restriction.getValue()))
                    break;
                return cb.like(cb.lower(
                        getExpressionByKey(root, restriction.getKey()).as(String.class)), "%" + restriction.getValue().toString().toLowerCase());
            }
            case "contains": {
                if (isEmpty(restriction.getValue()))
                    break;
                return cb.like(cb.lower(
                        getExpressionByKey(root, restriction.getKey()).as(String.class)), "%" + restriction.getValue().toString().toLowerCase() + "%");
            }
            case "equals": {
                if (isEmpty(restriction.getValue()))
                    break;
                return cb.equal(getExpressionByKey(root, restriction.getKey()), restriction.getValue());
            }
            case "in": {
                if (isEmpty(restriction.getValue()) || !Collection.class.isAssignableFrom(restriction.getValue().getClass()))
                    break;

                return getExpressionByKey(root, restriction.getKey()).in((Collection) restriction.getValue());
            }
        }
        return null;
    }

    private Date getDate(Object value) {
        if (value == null)
            return null;
        if (value instanceof Date)
            return (Date) value;
        else if (Long.class.isAssignableFrom(value.getClass()))
            return new Date((Long) value);
        else
            return null;
    }

    public Expression<T> getExpressionByKey(Root<T> root, String key) {
        String[] split = key.split("\\.");
        Path<T> result = root;
        for (String s : split)
            result = result.get(s);
        return result;
    }
}
