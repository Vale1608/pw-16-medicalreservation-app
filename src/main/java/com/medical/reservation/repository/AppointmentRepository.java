package com.medical.reservation.repository;

import com.medical.reservation.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatient_PatientId(Long patientId);

    List<Appointment> findByDoctor_DoctorId(Long doctorId);

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    List<Appointment> findByAppointmentDateBetween(LocalDate startDate, LocalDate endDate);

    List<Appointment> findByDoctor_DoctorIdAndAppointmentDate(Long doctorId, LocalDate appointmentDate);

    boolean existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    boolean existsByPatient_PatientIdAndAppointmentDateAndAppointmentTime(
            Long patientId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );
}