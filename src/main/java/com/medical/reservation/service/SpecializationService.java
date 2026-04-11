package com.medical.reservation.service;

import com.medical.reservation.exception.ResourceNotFoundException;
import com.medical.reservation.model.Specialization;
import com.medical.reservation.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }

    public Specialization getSpecializationById(Long specializationId) {
        return specializationRepository.findById(specializationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Specialization not found with id: " + specializationId
                ));
    }

    public Specialization createSpecialization(Specialization specialization) {
        if (specializationRepository.existsByName(specialization.getName())) {
            throw new IllegalArgumentException("A specialization with this name already exists.");
        }

        return specializationRepository.save(specialization);
    }

    public Specialization updateSpecialization(Long specializationId, Specialization updatedSpecialization) {
        Specialization specialization = getSpecializationById(specializationId);

        specialization.setName(updatedSpecialization.getName());
        specialization.setDescription(updatedSpecialization.getDescription());

        return specializationRepository.save(specialization);
    }

    public void deleteSpecialization(Long specializationId) {
        Specialization specialization = getSpecializationById(specializationId);
        specializationRepository.delete(specialization);
    }
}