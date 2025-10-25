package com.cmv.vetclinic.modules.veterinaryStats.service;

import java.util.List;

import com.cmv.vetclinic.modules.veterinaryStats.dto.AppointmentCount;
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
}