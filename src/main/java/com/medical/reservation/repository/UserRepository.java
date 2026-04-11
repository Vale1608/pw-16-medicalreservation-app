package com.medical.reservation.repository;

import com.medical.reservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByPatient_PatientId(Long patientId);

    Optional<User> findByPatient_PatientId(Long patientId);
}