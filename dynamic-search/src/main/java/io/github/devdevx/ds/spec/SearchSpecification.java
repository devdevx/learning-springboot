package io.github.devdevx.ds.spec;

import jakarta.persistence.criteria.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.domain.Specification;

public class SearchSpecification<T> implements Specification<T> {

    private static final ConversionService conversionService;
    static {
        var service = new DefaultConversionService();
        Jsr310Converters.getConvertersToRegister().forEach(service::addConverter);
        conversionService = service;
    }

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

        var value = convert(path.getJavaType(), criteria.getValue());

        return switch (criteria.getOperation()) {
            case like -> builder.like((Expression) path, "%" + value + "%");
            case equal -> builder.equal(path, value);
            case greaterThan -> builder.greaterThan(path, (Comparable) value);
            case lessThan -> builder.lessThan(path, (Comparable) value);
            case greaterThanOrEqual -> builder.greaterThanOrEqualTo(path, (Comparable) value);
            case lessThanOrEqual -> builder.lessThanOrEqualTo(path, (Comparable) value);
            case in -> path.in((Object[]) value);
            case isNull -> builder.isNull(path);
        };
    }

    private Object convert(Class<?> targetType, Object value) {
        if (value instanceof Object[] arrayValue) {
            var convertedArray = new Object[arrayValue.length];
            for (int i = 0; i < arrayValue.length; i++) {
                convertedArray[i] = conversionService.convert(arrayValue[i], targetType);
            }
            return convertedArray;
        } else {
            return conversionService.convert(value, targetType);
        }
    }
}

