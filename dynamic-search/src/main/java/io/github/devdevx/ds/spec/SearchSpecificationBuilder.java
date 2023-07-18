package io.github.devdevx.ds.spec;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchSpecificationBuilder<T> {
    private final List<SearchCriteria> criteria;

    public SearchSpecificationBuilder() {
        this.criteria = new ArrayList<>();
    }

    public SearchSpecificationBuilder<T> equal(String key, Object value) {
        criteria.add(new SearchCriteria(key, SearchOperation.equal, value));
        return this;
    }

    public SearchSpecificationBuilder<T> like(String key, Object value) {
        criteria.add(new SearchCriteria(key, SearchOperation.like, value));
        return this;
    }

    public SearchSpecificationBuilder<T> greaterThan(String key, Object value) {
        criteria.add(new SearchCriteria(key, SearchOperation.greaterThan, value));
        return this;
    }

    public SearchSpecificationBuilder<T> lessThan(String key, Object value) {
        criteria.add(new SearchCriteria(key, SearchOperation.lessThan, value));
        return this;
    }

    public SearchSpecificationBuilder<T> greaterThanOrEqual(String key, Object value) {
        criteria.add(new SearchCriteria(key, SearchOperation.greaterThanOrEqual, value));
        return this;
    }

    public SearchSpecificationBuilder<T> lessThanOrEqual(String key, Object value) {
        criteria.add(new SearchCriteria(key, SearchOperation.lessThanOrEqual, value));
        return this;
    }

    public SearchSpecificationBuilder<T> in(String key, List<?> values) {
        return in(key, values.toArray());
    }

    public SearchSpecificationBuilder<T> in(String key, Object[] values) {
        criteria.add(new SearchCriteria(key, SearchOperation.in, values));
        return this;
    }

    public SearchSpecificationBuilder<T> isNull(String key) {
        criteria.add(new SearchCriteria(key, SearchOperation.isNull, null));
        return this;
    }

    public Specification<T> build() {
        if (criteria.isEmpty()) {
            return null;
        }

        List<Specification<T>> specifications = criteria.stream()
                .map(SearchSpecification<T>::new)
                .collect(Collectors.toList());

        return Specification.allOf(specifications);
    }
}

