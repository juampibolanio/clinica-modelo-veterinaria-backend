package com.cmv.vetclinic.modules.veterinaryStats.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.cmv.vetclinic.modules.veterinaryStats.service.VeterinaryStatsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class VeterinaryStatsController {

    private final VeterinaryStatsService service;

    // STAT-01
    @GetMapping("/appointments-per-vet")
    public ResponseEntity<List<AppointmentCount>> appointmentsPerVet(
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(service.getAppointmentsPerVet(limit));
    }

    // STAT-02
    @GetMapping("/vaccines-per-month")
    public ResponseEntity<List<VaccinesPerMonth>> vaccinesPerMonth(
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(service.getVaccinesPerMonth(year));
    }

    // STAT-03
    @GetMapping("/top-products")
    public ResponseEntity<List<TopProduct>> topProducts(
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(service.getTopProducts(limit));
    }

    // STAT-04 (solo ADMIN)
    @GetMapping("/pets-per-owner")
    public ResponseEntity<List<PetsPerOwner>> petsPerOwner(
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(service.getPetsPerOwner(limit));
    }

    // STAT-05
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/top-diagnoses")
    public ResponseEntity<List<TopDiagnosis>> topDiagnoses(
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(service.getTopDiagnoses(limit));
    }

    // STAT-06
    @GetMapping("/pets-by-species")
    public ResponseEntity<List<PetsBySpecies>> petsBySpecies() {
        return ResponseEntity.ok(service.getPetsBySpecies());
    }

    // STAT-07
    @GetMapping("/pets-by-gender")
    public ResponseEntity<List<PetsByGender>> petsByGender() {
        return ResponseEntity.ok(service.getPetsByGender());
    }

    // STAT-08
    @GetMapping("/appointments-by-period")
    public ResponseEntity<List<AppointmentsByPeriod>> appointmentsByPeriod(
            @RequestParam(defaultValue = "month") String type,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(service.getAppointmentsByPeriod(type, year));
    }

    // STAT-09
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/average-pet-age")
    public ResponseEntity<AveragePetAge> averagePetAge() {
        return ResponseEntity.ok(service.getAveragePetAge());
    }

    // STAT-10
    @GetMapping("/top-diagnoses-by-species")
    public ResponseEntity<List<DiagnosisBySpecies>> topDiagnosesBySpecies(
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(service.getTopDiagnosesBySpecies(limit));
    }

    // DASHBOARD
    @GetMapping("/dashboard")
    public DashboardStatsResponse getDashboard() {
        return service.getDashboardStats();
    }
}