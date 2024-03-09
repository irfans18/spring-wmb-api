package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.entity.DinningTable;
import com.enigma.wmb_api.model.request.DinningTableRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DinningTableSpecification {

    public static Specification<DinningTable> getSpecification(DinningTableRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), request.getId()));
            }
            if (request.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();

        };
    }
}
