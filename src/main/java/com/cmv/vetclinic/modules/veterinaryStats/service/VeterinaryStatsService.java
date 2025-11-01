package com.cmv.vetclinic.modules.veterinaryStats.service;

import java.util.List;

import com.cmv.vetclinic.modules.veterinaryStats.dto.AppointmentCount;
import com.cmv.vetclinic.modules.veterinaryStats.dto.AppointmentsByPeriod;
import com.cmv.vetclinic.modules.veterinaryStats.dto.AveragePetAge;
import com.cmv.vetclinic.modules.veterinaryStats.dto.DashboardStatsResponse;
import com.cmv.vetclinic.modules.veterinaryStats.dto.DiagnosisBySpecies;
import com.cmv.vetclinic.modules.veterinaryStats.dto.PetsByGender;
import com.cmv.vetclinic.modules.veterinaryStats.dto.PetsBySpecies;
import com.cmv.vetclinic.modules.veterinaryStats.dto.PetsPerOwner;
import com.cmv.vetclinic.modules.veterinaryStats.dto.TopDiagnosis;
import com.cmv.vetclinic.modules.veterinaryStats.dto.TopProduct;
import com.cmv.vetclinic.modules.veterinaryStats.dto.VaccinesPerMonth;

public interface VeterinaryStatsService {
    List<AppointmentCount> getAppointmentsPerVet(Integer limit);
    List<VaccinesPerMonth> getVaccinesPerMonth(Integer year);
    List<TopProduct> getTopProducts(Integer limit);
    List<PetsPerOwner> getPetsPerOwner(Integer limit);
    List<TopDiagnosis> getTopDiagnoses(Integer limit);

    List<PetsBySpecies> getPetsBySpecies();
    List<PetsByGender> getPetsByGender();
    List<AppointmentsByPeriod> getAppointmentsByPeriod(String type, Integer year);
    AveragePetAge getAveragePetAge();
    List<DiagnosisBySpecies> getTopDiagnosesBySpecies(Integer limit);
    DashboardStatsResponse getDashboardStats();
}