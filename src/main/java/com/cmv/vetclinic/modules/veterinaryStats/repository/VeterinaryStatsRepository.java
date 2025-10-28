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

    // ------------------------------------------------------
    // STAT-01 Turnos por veterinario
    // ------------------------------------------------------
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

    // ------------------------------------------------------
    // STAT-02 Vacunas aplicadas por mes
    // ------------------------------------------------------
    public List<VaccinesPerMonth> vaccinesAppliedPerMonth(Integer year) {
        return entityManager.createQuery("""
                    select new com.cmv.vetclinic.modules.veterinaryStats.dto.VaccinesPerMonth(
                        CAST(FUNCTION('date_part', 'year', av.date) AS integer),
                        CAST(FUNCTION('date_part', 'month', av.date) AS integer),
                        count(av)
                    )
                    from AppliedVaccine av
                    where (:year is null or CAST(FUNCTION('date_part', 'year', av.date) AS integer) = :year)
                    group by FUNCTION('date_part', 'year', av.date), FUNCTION('date_part', 'month', av.date)
                    order by FUNCTION('date_part', 'year', av.date), FUNCTION('date_part', 'month', av.date)
                """, VaccinesPerMonth.class)
                .setParameter("year", year)
                .getResultList();
    }

    // ------------------------------------------------------
    // STAT-03 Productos más usados
    // ------------------------------------------------------
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

    // ------------------------------------------------------
    // STAT-04 Mascotas por dueño
    // ------------------------------------------------------
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

    // ------------------------------------------------------
    // STAT-05 Diagnósticos más frecuentes
    // ------------------------------------------------------
    public List<TopDiagnosis> topDiagnoses() {
        return entityManager.createQuery("""
                    select new com.cmv.vetclinic.modules.veterinaryStats.dto.TopDiagnosis(
                        lower(trim(both from ch.diagnosis)),
                        count(ch)
                    )
                    from ClinicalHistory ch
                    where ch.diagnosis is not null
                      and length(trim(both from ch.diagnosis)) > 0
                    group by lower(trim(both from ch.diagnosis))
                    order by count(ch) desc
                """, TopDiagnosis.class).getResultList();
    }

    // ------------------------------------------------------
    // STAT-06 Mascotas por especie
    // ------------------------------------------------------
    public List<PetsBySpecies> petsBySpecies() {
        return entityManager.createQuery("""
                    select new com.cmv.vetclinic.modules.veterinaryStats.dto.PetsBySpecies(
                        p.species,
                        count(p)
                    )
                    from Pet p
                    group by p.species
                    order by count(p) desc
                """, PetsBySpecies.class).getResultList();
    }

    // ------------------------------------------------------
    // STAT-07 Mascotas por género
    // ------------------------------------------------------
    public List<PetsByGender> petsByGender() {
        return entityManager.createQuery("""
                    select new com.cmv.vetclinic.modules.veterinaryStats.dto.PetsByGender(
                        CAST(p.gender as string),
                        count(p)
                    )
                    from Pet p
                    group by p.gender
                    order by count(p) desc
                """, PetsByGender.class).getResultList();
    }

    // ------------------------------------------------------
    // STAT-08a Turnos por mes
    // ------------------------------------------------------
    public List<AppointmentsByPeriod> appointmentsByMonth(Integer year) {
        return entityManager.createQuery("""
                    select new com.cmv.vetclinic.modules.veterinaryStats.dto.AppointmentsByPeriod(
                        concat(
                            CAST(FUNCTION('date_part', 'year', a.date) AS integer),
                            '-',
                            FUNCTION('lpad', CAST(FUNCTION('date_part', 'month', a.date) AS text), 2, '0')
                        ),
                        count(a)
                    )
                    from Appointment a
                    where (:year is null or CAST(FUNCTION('date_part', 'year', a.date) AS integer) = :year)
                    group by FUNCTION('date_part', 'year', a.date), FUNCTION('date_part', 'month', a.date)
                    order by FUNCTION('date_part', 'year', a.date), FUNCTION('date_part', 'month', a.date)
                """, AppointmentsByPeriod.class)
                .setParameter("year", year)
                .getResultList();
    }

    // ------------------------------------------------------
    // STAT-08b Turnos por semana
    // ------------------------------------------------------
    public List<AppointmentsByPeriod> appointmentsByWeek(Integer year) {
        return entityManager.createQuery("""
                    select new com.cmv.vetclinic.modules.veterinaryStats.dto.AppointmentsByPeriod(
                        concat('Semana ', CAST(FUNCTION('date_part', 'week', a.date) AS integer)),
                        count(a)
                    )
                    from Appointment a
                    where (:year is null or CAST(FUNCTION('date_part', 'year', a.date) AS integer) = :year)
                    group by FUNCTION('date_part', 'week', a.date)
                    order by FUNCTION('date_part', 'week', a.date)
                """, AppointmentsByPeriod.class)
                .setParameter("year", year)
                .getResultList();
    }

    // ------------------------------------------------------
    // STAT-09 Promedio de edad de mascotas
    // ------------------------------------------------------
    public AveragePetAge averagePetAge() {
        Double avg = entityManager.createQuery(
                """
                            select avg(CAST(FUNCTION('date_part', 'year', FUNCTION('age', current_date, p.birthDate)) AS double))
                            from Pet p
                            where p.birthDate is not null
                        """,
                Double.class).getSingleResult();

        return new AveragePetAge(avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0);
    }

    // ------------------------------------------------------
    // STAT-10 Diagnósticos más frecuentes por especie
    // ------------------------------------------------------
    public List<DiagnosisBySpecies> topDiagnosesBySpecies(Integer limit) {
        return entityManager.createQuery("""
                    select new com.cmv.vetclinic.modules.veterinaryStats.dto.DiagnosisBySpecies(
                        lower(trim(both from p.species)),
                        lower(trim(both from ch.diagnosis)),
                        count(ch)
                    )
                    from ClinicalHistory ch
                    join ch.pet p
                    where ch.diagnosis is not null
                      and length(trim(both from ch.diagnosis)) > 0
                    group by lower(trim(both from p.species)), lower(trim(both from ch.diagnosis))
                    order by count(ch) desc
                """, DiagnosisBySpecies.class)
                .setMaxResults(limit != null && limit > 0 ? limit : 10)
                .getResultList();
    }

}
