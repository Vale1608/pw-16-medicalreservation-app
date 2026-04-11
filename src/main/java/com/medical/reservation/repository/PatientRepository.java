package com.medical.reservation.repository;


import com.medical.reservation.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByTaxCode(String taxCode);

    boolean existsByEmail(String email);

    boolean existsByTaxCode(String taxCode);

    List<Patient> findByLastNameContainingIgnoreCase(String lastName);
}