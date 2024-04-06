package com.example.russionTest.repos;

import com.example.russionTest.domain.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolClassRepo extends JpaRepository<SchoolClass, Integer> {
     SchoolClass findByName(String name);
}