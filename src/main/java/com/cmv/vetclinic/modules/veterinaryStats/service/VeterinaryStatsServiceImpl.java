package com.cmv.vetclinic.modules.veterinaryStats.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmv.vetclinic.modules.veterinaryStats.dto.AppointmentCount;
import com.cmv.vetclinic.modules.veterinaryStats.dto.AppointmentsByPeriod;
import com.cmv.vetclinic.modules.veterinaryStats.dto.AveragePetAge;
import com.cmv.vetclinic.modules.veterinaryStats.dto.DiagnosisBySpecies;
import com.cmv.vetclinic.modules.veterinaryStats.dto.PetsByGender;
import com.cmv.vetclinic.modules.veterinaryStats.dto.PetsBySpecies;
import com.cmv.vetclinic.modules.veterinaryStats.dto.PetsPerOwner;
import com.cmv.vetclinic.modules.veterinaryStats.dto.TopDiagnosis;
import com.cmv.vetclinic.modules.veterinaryStats.dto.TopProduct;
import com.cmv.vetclinic.modules.veterinaryStats.dto.VaccinesPerMonth;
import com.cmv.vetclinic.modules.veterinaryStats.repository.VeterinaryStatsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VeterinaryStatsServiceImpl implements VeterinaryStatsService {

    private final VeterinaryStatsRepository repo;

    @Override
    public List<AppointmentCount> getAppointmentsPerVet(Integer limit) {
        var list = repo.countAppointmentsByVet();
        return applyLimit(list, limit);
    }

    @Override
    public List<VaccinesPerMonth> getVaccinesPerMonth(Integer year) {
        return repo.vaccinesAppliedPerMonth(year);
    }

    @Override
    public List<TopProduct> getTopProducts(Integer limit) {
        var list = repo.topUsedProducts();
        return applyLimit(list, limit);
    }

    @Override
    public List<PetsPerOwner> getPetsPerOwner(Integer limit) {
        var list = repo.petsPerOwner();
        return applyLimit(list, limit);
    }

    @Override
    public List<TopDiagnosis> getTopDiagnoses(Integer limit) {
        var list = repo.topDiagnoses();
        return applyLimit(list, limit);
    }

    private static <T> List<T> applyLimit(List<T> source, Integer limit) {
        if (limit == null || limit <= 0 || limit >= source.size())
            return source;
        return source.subList(0, limit);
    }

    @Override
    public List<PetsBySpecies> getPetsBySpecies() {
        return repo.petsBySpecies();
    }

    @Override
    public List<PetsByGender> getPetsByGender() {
        return repo.petsByGender();
    }

    @Override
    public List<AppointmentsByPeriod> getAppointmentsByPeriod(String type, Integer year) {
        if ("week".equalsIgnoreCase(type)) {
            return repo.appointmentsByWeek(year);
        }
        return repo.appointmentsByMonth(year); // por defecto mensual
    }

    @Override
    public AveragePetAge getAveragePetAge() {
        return repo.averagePetAge();
    }

    @Override
    public List<DiagnosisBySpecies> getTopDiagnosesBySpecies(Integer limit) {
        return repo.topDiagnosesBySpecies(limit);
    }
}
