package com.cmv.vetclinic.modules.clinicalHistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cmv.vetclinic.modules.clinicalHistory.model.ClinicalHistory;

public interface ClinicalHistoryRepository
extends JpaRepository<ClinicalHistory, Long>, JpaSpecificationExecutor<ClinicalHistory> {

        void deleteAllByPetId(Long petId);
}
