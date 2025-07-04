package com.onlinebanking.minorbeneficiaries.service;

import com.onlinebanking.minorbeneficiaries.model.Beneficiary;
import com.onlinebanking.minorbeneficiaries.repository.BeneficiaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository repository;

    public BeneficiaryService(BeneficiaryRepository repository) {
        this.repository = repository;
    }

    public List<Beneficiary> getAll() {
        return repository.findAll();
    }

    public Optional<Beneficiary> getById(Long id) {
        return repository.findById(id);
    }

    public Beneficiary create(Beneficiary beneficiary) {
        beneficiary.setCreatedAt(java.time.LocalDateTime.now());
        beneficiary.setUpdatedAt(java.time.LocalDateTime.now());
        return repository.save(beneficiary);
    }

    public Beneficiary update(Long id, Beneficiary updated) {
        return repository.findById(id).map(existing -> {
            updated.setBeneficiaryId(id);
            updated.setCreatedAt(existing.getCreatedAt());
            updated.setUpdatedAt(java.time.LocalDateTime.now());
            return repository.save(updated);
        }).orElseThrow();
    }

    public void delete(Long id) {
        repository.findById(id).ifPresent(b -> {
            b.setDeletedAt(java.time.LocalDateTime.now());
            repository.save(b);
        });
    }
}