package com.cmv.vetclinic.modules.pet.service;

import org.springframework.data.jpa.domain.Specification;

import com.cmv.vetclinic.modules.pet.model.Genders;
import com.cmv.vetclinic.modules.pet.model.Pet;

import jakarta.persistence.criteria.Predicate;

public class PetSpecification {
    
    public static Specification<Pet> filterBy(String name, 
            String species, 
            String breed, 
            String gender, 
            Long ownerId, 
            Integer ageMin, 
            Integer ageMax, 
            Double weightMin, 
            Double weightMax) {
        
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (name != null && !name.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (species != null && !species.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("species")), "%" + species.toLowerCase() + "%"));
            }
            if (breed != null && !breed.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("breed")), "%" + breed.toLowerCase() + "%"));
            }
            if (gender != null && !gender.isBlank()) {
                try {
                    Genders g = Genders.valueOf(gender.toUpperCase().trim());
                    p = cb.and(p, cb.equal(root.get("gender"), g));
                } catch (IllegalArgumentException ignored) {
                    // invalid gender value -> no apply filter
                }
            }
            if (ownerId != null) {
                p = cb.and(p, cb.equal(root.get("owner").get("id"), ownerId));
            }
            if (ageMin != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("age"), ageMin));
            }
            if (ageMax != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("age"), ageMax));
            }
            if (weightMin != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("weight"), weightMin));
            }
            if (weightMax != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("weight"), weightMax));
            }

            return p;
        };
    }
}
