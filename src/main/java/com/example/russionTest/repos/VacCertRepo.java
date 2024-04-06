package com.example.russionTest.repos;

import com.example.russionTest.domain.User;
import com.example.russionTest.domain.VaccinationCertifficate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacCertRepo extends JpaRepository<VaccinationCertifficate, Integer> {
    VaccinationCertifficate findByUser_Username(String username);
}