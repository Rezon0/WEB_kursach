package com.example.russionTest.repos;

import com.example.russionTest.domain.ClassVaccination;
import com.example.russionTest.domain.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacRepository extends JpaRepository<Vaccination, Integer> {
    Vaccination findByName(String name);

    List<Vaccination> findAll();
}
