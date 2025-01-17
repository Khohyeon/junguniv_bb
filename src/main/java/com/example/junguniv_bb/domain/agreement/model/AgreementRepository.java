package com.example.junguniv_bb.domain.agreement.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    List<Agreement> findAllByAgreementType(String agreementType);
}
