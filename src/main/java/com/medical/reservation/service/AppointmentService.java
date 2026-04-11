package com.medical.reservation.service;

import com.medical.reservation.exception.ResourceNotFoundException;
import com.medical.reservation.model.Appointment;
import com.medical.reservation.model.Doctor;
import com.medical.reservation.model.Patient;
import com.medical.reservation.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final CurrentUserService currentUserService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientService patientService,
                              DoctorService doctorService,
                              CurrentUserService currentUserService) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.currentUserService = currentUserService;
    }

    public List<Appointment> getAllAppointments() {
        if (currentUserService.isAdmin()) {
            return appointmentRepository.findAll();
        }

        Long currentPatientId = currentUserService.getCurrentPatientId();
        return appointmentRepository.findByPatient_PatientId(currentPatientId);
    }

    public Appointment getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + appointmentId
                ));

        validateAppointmentAccess(appointment);

        return appointment;
    }

    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        if (!currentUserService.isAdmin()) {
            Long currentPatientId = currentUserService.getCurrentPatientId();

            if (!currentPatientId.equals(patientId))
                throw new org.springframework.security.access.AccessDeniedException(
                        "You can only access your own appointments."
                );
        }

        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        if (currentUserService.isAdmin()) {
            return appointmentRepository.findByDoctor_DoctorId(doctorId);
        }

        Long currentPatientId = currentUserService.getCurrentPatientId();

        return appointmentRepository.findByDoctor_DoctorId(doctorId)
                .stream()
                .filter(appointment -> appointment.getPatient().getPatientId().equals(currentPatientId))
                .toList();
    }

    public List<Appointment> getAppointmentsByDate(LocalDate appointmentDate) {
        if (currentUserService.isAdmin()) {
            return appointmentRepository.findByAppointmentDate(appointmentDate);
        }

        Long currentPatientId = currentUserService.getCurrentPatientId();

        return appointmentRepository.findByAppointmentDate(appointmentDate)
                .stream()
                .filter(appointment -> appointment.getPatient().getPatientId().equals(currentPatientId))
                .toList();
    }

    public Appointment createAppointment(Appointment appointment) {
        if (appointment.getPatient() == null || appointment.getPatient().getPatientId() == null) {
            throw new IllegalArgumentException("Patient is required.");
        }

        if (appointment.getDoctor() == null || appointment.getDoctor().getDoctorId() == null) {
            throw new IllegalArgumentException("Doctor is required.");
        }

        if (appointment.getAppointmentDate() == null) {
            throw new IllegalArgumentException("Appointment date is required.");
        }

        if (appointment.getAppointmentTime() == null) {
            throw new IllegalArgumentException("Appointment time is required.");
        }

        Long requestedPatientId = appointment.getPatient().getPatientId();

        if (!currentUserService.isAdmin()) {
            Long currentPatientId = currentUserService.getCurrentPatientId();

            if (!currentPatientId.equals(requestedPatientId))
                throw new org.springframework.security.access.AccessDeniedException(
                        "You can only access your own appointments."
                );
        }

        Patient patient = patientService.getPatientById(requestedPatientId);
        Doctor doctor = doctorService.getDoctorById(appointment.getDoctor().getDoctorId());

        boolean doctorAlreadyBusy = appointmentRepository
                .existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(
                        doctor.getDoctorId(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime()
                );

        if (doctorAlreadyBusy) {
            throw new IllegalArgumentException("The doctor already has an appointment at the selected date and time.");
        }

        boolean patientAlreadyBusy = appointmentRepository
                .existsByPatient_PatientIdAndAppointmentDateAndAppointmentTime(
                        patient.getPatientId(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime()
                );

        if (patientAlreadyBusy) {
            throw new IllegalArgumentException("The patient already has an appointment at the selected date and time.");
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        if (appointment.getStatus() == null || appointment.getStatus().isBlank()) {
            appointment.setStatus("BOOKED");
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long appointmentId, Appointment updatedAppointment) {
        Appointment appointment = getAppointmentById(appointmentId);

        validateAppointmentAccess(appointment);

        if (updatedAppointment.getAppointmentDate() != null) {
            appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
        }

        if (updatedAppointment.getAppointmentTime() != null) {
            appointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
        }

        if (updatedAppointment.getStatus() != null && !updatedAppointment.getStatus().isBlank()) {
            if (!currentUserService.isAdmin() && !"CANCELLED".equalsIgnoreCase(updatedAppointment.getStatus())) {
                throw new IllegalArgumentException("Patients can only cancel their appointments.");
            }

            appointment.setStatus(updatedAppointment.getStatus());
        }

        appointment.setNotes(updatedAppointment.getNotes());

        if (currentUserService.isAdmin()) {
            if (updatedAppointment.getPatient() != null && updatedAppointment.getPatient().getPatientId() != null) {
                Patient patient = patientService.getPatientById(updatedAppointment.getPatient().getPatientId());
                appointment.setPatient(patient);
            }

            if (updatedAppointment.getDoctor() != null && updatedAppointment.getDoctor().getDoctorId() != null) {
                Doctor doctor = doctorService.getDoctorById(updatedAppointment.getDoctor().getDoctorId());
                appointment.setDoctor(doctor);
            }
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);

        validateAppointmentAccess(appointment);

        appointment.setStatus("CANCELLED");
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);

        validateAppointmentAccess(appointment);

        appointmentRepository.delete(appointment);
    }

    private void validateAppointmentAccess(Appointment appointment) {
        if (currentUserService.isAdmin()) {
            return;
        }

        Long currentPatientId = currentUserService.getCurrentPatientId();

        if (!appointment.getPatient().getPatientId().equals(currentPatientId))
            throw new org.springframework.security.access.AccessDeniedException(
                    "You can only access your own appointments."
            );
    }
}