package com.cmv.vetclinic.modules.appliedVaccine.service;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import com.cmv.vetclinic.modules.appliedVaccine.model.AppliedVaccine;

public class AppliedVaccineSpecification {

    public static Specification<AppliedVaccine> byPetId(Long petId) {
        return (root, q, cb) -> petId == null
                ? null
                : cb.equal(root.get("pet").get("id"), petId);
    }

    public static Specification<AppliedVaccine> byVeterinarianId(Long vetId) {
        return (root, q, cb) -> vetId == null
                ? null
                : cb.equal(root.get("veterinarian").get("id"), vetId);
    }

    public static Specification<AppliedVaccine> byProductId(Long productId) {
        return (root, q, cb) -> productId == null
                ? null
                : cb.equal(root.get("product").get("id"), productId);
    }

    public static Specification<AppliedVaccine> fromDate(LocalDate from) {
        return (root, q, cb) -> from == null
                ? null
                : cb.greaterThanOrEqualTo(root.get("date"), from);
    }

    public static Specification<AppliedVaccine> toDate(LocalDate to) {
        return (root, q, cb) -> to == null
                ? null
                : cb.lessThanOrEqualTo(root.get("date"), to);
    }

    public static Specification<AppliedVaccine> filter(
            Long petId,
            Long vetId,
            Long productId,
            LocalDate from,
            LocalDate to
    ) {
        return Specification.allOf(
                byPetId(petId),
                byVeterinarianId(vetId),
                byProductId(productId),
                fromDate(from),
                toDate(to)
        );
    }
}
