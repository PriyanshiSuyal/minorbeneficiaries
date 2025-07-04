package com.onlinebanking.minorbeneficiaries.repository;

import com.onlinebanking.minorbeneficiaries.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
}