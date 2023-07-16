package io.github.devdevx.ds.spec;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class SearchSpecification<T> implements Specification<T> {
    private final SearchCriteria criteria;

    public SearchSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        var nestedProperties = criteria.getKey().split("\\.");
        Expression<Comparable> path = null;
        for (String property : nestedProperties) {
            if (path == null) path = root.get(property);
            else path = ((Path<?>) path).get(property);
        }
        return switch (criteria.getOperation()) {
            case like -> builder.like((Expression) path, "%" + criteria.getValue() + "%");
            case equal -> builder.equal(path, criteria.getValue());
            case greaterThan -> builder.greaterThan(path, (Comparable) criteria.getValue());
            case lessThan -> builder.lessThan(path, (Comparable) criteria.getValue());
            case greaterThanOrEqual -> builder.greaterThanOrEqualTo(path, (Comparable) criteria.getValue());
            case lessThanOrEqual -> builder.lessThanOrEqualTo(path, (Comparable) criteria.getValue());
            case in -> path.in((Object[]) criteria.getValue());
            case isNull -> builder.isNull(path);
        };
    }

}

