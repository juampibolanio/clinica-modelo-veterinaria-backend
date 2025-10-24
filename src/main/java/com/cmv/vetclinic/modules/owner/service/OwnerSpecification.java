package com.cmv.vetclinic.modules.owner.service;


import org.springframework.data.jpa.domain.Specification;

import com.cmv.vetclinic.modules.owner.model.Owner;

import jakarta.persistence.criteria.Predicate;

public class OwnerSpecification {

    public static Specification<Owner> filterBy(String name, String surname, String documentNumber) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (surname != null && !surname.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + surname.toLowerCase() + "%"));
            }

            if (documentNumber != null && !documentNumber.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(root.get("documentNumber"), "%" + documentNumber + "%"));
            }

            return predicate;
        };
    }
}
