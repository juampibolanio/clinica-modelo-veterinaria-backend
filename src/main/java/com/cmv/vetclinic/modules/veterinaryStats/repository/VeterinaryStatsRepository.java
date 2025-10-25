package com.cmv.vetclinic.modules.veterinaryStats.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.cmv.vetclinic.modules.veterinaryStats.dto.*;

@Repository
public class VeterinaryStatsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<AppointmentCount> countAppointmentsByVet() {
        return entityManager.createQuery("""
            select new com.cmv.vetclinic.modules.veterinaryStats.dto.AppointmentCount(
                v.id,
                concat(v.name, ' ', v.surname),
                count(a)
            )
            from Appointment a
            join a.veterinarian v
            group by v.id, v.name, v.surname
            order by count(a) desc
        """, AppointmentCount.class).getResultList();
    }

    public List<VaccinesPerMonth> vaccinesAppliedPerMonth(Integer year) {
        return entityManager.createQuery("""
            select new com.cmv.vetclinic.modules.veterinaryStats.dto.VaccinesPerMonth(
                YEAR(av.date),
                MONTH(av.date),
                count(av)
            )
            from AppliedVaccine av
            where (:year is null or YEAR(av.date) = :year)
            group by YEAR(av.date), MONTH(av.date)
            order by YEAR(av.date), MONTH(av.date)
        """, VaccinesPerMonth.class)
        .setParameter("year", year)
        .getResultList();
    }

    public List<TopProduct> topUsedProducts() {
        return entityManager.createQuery("""
            select new com.cmv.vetclinic.modules.veterinaryStats.dto.TopProduct(
                p.id, p.name, count(av)
            )
            from AppliedVaccine av
            join av.product p
            group by p.id, p.name
            order by count(av) desc
        """, TopProduct.class).getResultList();
    }

    public List<PetsPerOwner> petsPerOwner() {
        return entityManager.createQuery("""
            select new com.cmv.vetclinic.modules.veterinaryStats.dto.PetsPerOwner(
                o.id,
                concat(o.name, ' ', o.surname),
                count(p)
            )
            from Pet p
            join p.owner o
            group by o.id, o.name, o.surname
            order by count(p) desc
        """, PetsPerOwner.class).getResultList();
    }

    public List<TopDiagnosis> topDiagnoses() {
        return entityManager.createQuery("""
            select new com.cmv.vetclinic.modules.veterinaryStats.dto.TopDiagnosis(
                lower(trim(ch.diagnosis)),
                count(ch)
            )
            from ClinicalHistory ch
            where ch.diagnosis is not null and length(trim(ch.diagnosis)) > 0
            group by lower(trim(ch.diagnosis))
            order by count(ch) desc
        """, TopDiagnosis.class).getResultList();
    }
}
