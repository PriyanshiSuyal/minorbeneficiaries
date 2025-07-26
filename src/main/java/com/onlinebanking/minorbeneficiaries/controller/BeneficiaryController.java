package com.onlinebanking.minorbeneficiaries.controller;

import com.onlinebanking.minorbeneficiaries.model.Beneficiary;
import com.onlinebanking.minorbeneficiaries.service.BeneficiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService service;

    public BeneficiaryController(BeneficiaryService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<Beneficiary>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Beneficiary> create(@RequestBody Beneficiary beneficiary) {
        return ResponseEntity.ok(service.create(beneficiary));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Beneficiary> update(@PathVariable Long id, @RequestBody Beneficiary beneficiary) {
        return ResponseEntity.ok(service.update(id, beneficiary));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}