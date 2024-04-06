package com.example.russionTest.repos;

import com.example.russionTest.domain.VaccinationAndCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationAndCertificateRepository extends JpaRepository<VaccinationAndCertificate, Integer> {
    VaccinationAndCertificate findByVaccinationIdAndVaccinationCertifficateId(int vaccinationId, int vaccinationCertifficateId);

    List<VaccinationAndCertificate> findAll();
}
