package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.entity.User;
import com.enigma.wmb_api.model.request.UserRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> getSpecification(UserRequest request){
        return (root, cq, cb) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (request.getName() !=null) {
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
                predicates.add(namePredicate);
            }
            if (request.getPhoneNumber() != null) {
                Predicate phonePredicate = cb.equal(root.get("phoneNumber"), request.getPhoneNumber());
                predicates.add(phonePredicate);
            }
            if (request.getStatus() != null) {
                Predicate statusPredicate = cb.equal(root.get("status"), request.getStatus());
                predicates.add(statusPredicate);
            }

            return cq.where(predicates.toArray(new Predicate[]{})).getRestriction();
            // return null;
        };
    }
}
