package com.cmv.vetclinic.modules.appliedVaccine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cmv.vetclinic.modules.appliedVaccine.model.AppliedVaccine;

@Repository
public interface AppliedVaccineRepository extends JpaRepository<AppliedVaccine, Long>, JpaSpecificationExecutor<AppliedVaccine> {
    
}
