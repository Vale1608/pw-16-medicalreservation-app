package com.medical.reservation.service;

import com.medical.reservation.exception.ResourceNotFoundException;
import com.medical.reservation.model.Doctor;
import com.medical.reservation.model.Specialization;
import com.medical.reservation.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecializationService specializationService;

    public DoctorService(DoctorRepository doctorRepository, SpecializationService specializationService) {
        this.doctorRepository = doctorRepository;
        this.specializationService = specializationService;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + doctorId
                ));
    }

    public List<Doctor> getDoctorsByLastName(String lastName) {
        return doctorRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    public List<Doctor> getDoctorsBySpecializationId(Long specializationId) {
        return doctorRepository.findBySpecialization_SpecializationId(specializationId);
    }

    public List<Doctor> getDoctorsBySpecializationName(String specializationName) {
        return doctorRepository.findBySpecialization_NameIgnoreCase(specializationName);
    }

    public Doctor createDoctor(Doctor doctor) {
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new IllegalArgumentException("A doctor with this email already exists.");
        }

        if (doctor.getSpecialization() == null || doctor.getSpecialization().getSpecializationId() == null) {
            throw new IllegalArgumentException("Specialization is required.");
        }

        Specialization specialization = specializationService.getSpecializationById(
                doctor.getSpecialization().getSpecializationId()
        );

        doctor.setSpecialization(specialization);

        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long doctorId, Doctor updatedDoctor) {
        Doctor doctor = getDoctorById(doctorId);

        doctor.setFirstName(updatedDoctor.getFirstName());
        doctor.setLastName(updatedDoctor.getLastName());
        doctor.setEmail(updatedDoctor.getEmail());
        doctor.setPhone(updatedDoctor.getPhone());

        if (updatedDoctor.getSpecialization() != null &&
                updatedDoctor.getSpecialization().getSpecializationId() != null) {

            Specialization specialization = specializationService.getSpecializationById(
                    updatedDoctor.getSpecialization().getSpecializationId()
            );

            doctor.setSpecialization(specialization);
        }

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        doctorRepository.delete(doctor);
    }
}