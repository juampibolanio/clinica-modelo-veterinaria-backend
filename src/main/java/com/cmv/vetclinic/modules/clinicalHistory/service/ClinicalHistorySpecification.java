package com.cmv.vetclinic.modules.clinicalHistory.service;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import com.cmv.vetclinic.modules.clinicalHistory.model.ClinicalHistory;

public class ClinicalHistorySpecification {

    public static Specification<ClinicalHistory> byPetId(Long petId) {
        return (root, q, cb) -> petId == null ? null : cb.equal(root.get("pet").get("id"), petId);
    }

    public static Specification<ClinicalHistory> byVeterinarianId(Long vetId) {
        return (root, q, cb) -> vetId == null ? null : cb.equal(root.get("veterinarian").get("id"), vetId);
    }

    public static Specification<ClinicalHistory> byConsultationType(String type) {
        return (root, q, cb) -> (type == null || type.isBlank()) ? null
                : cb.like(cb.lower(root.get("consultationType")), "%" + type.toLowerCase() + "%");
    }

    public static Specification<ClinicalHistory> fromDate(LocalDate from) {
        return (root, q, cb) -> from == null ? null : cb.greaterThanOrEqualTo(root.get("date"), from);
    }

    public static Specification<ClinicalHistory> toDate(LocalDate to) {
        return (root, q, cb) -> to == null ? null : cb.lessThanOrEqualTo(root.get("date"), to);
    }

    public static Specification<ClinicalHistory> keyword(String kw) {
        return (root, q, cb) -> {
            if (kw == null || kw.isBlank()) return null;
            String like = "%" + kw.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("consultationReason")), like),
                    cb.like(cb.lower(root.get("diagnosis")), like),
                    cb.like(cb.lower(root.get("treatment")), like),
                    cb.like(cb.lower(root.get("observations")), like)
            );
        };
    }

    public static Specification<ClinicalHistory> filter(
            Long petId,
            Long veterinarianId,
            String consultationType,
            LocalDate fromDate,
            LocalDate toDate,
            String keyword
    ) {
        return Specification.allOf(
                byPetId(petId),
                byVeterinarianId(veterinarianId),
                byConsultationType(consultationType),
                fromDate(fromDate),
                toDate(toDate),
                keyword(keyword)
        );
    }
}
