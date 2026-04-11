package com.medical.reservation.repository;


import com.medical.reservation.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Doctor> findByLastNameContainingIgnoreCase(String lastName);

    List<Doctor> findBySpecialization_SpecializationId(Long specializationId);

    List<Doctor> findBySpecialization_NameIgnoreCase(String name);
}
