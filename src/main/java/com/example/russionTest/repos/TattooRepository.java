package com.example.russionTest.repos;

import com.example.russionTest.domain.TattooCatalog;
import com.example.russionTest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TattooRepository extends JpaRepository<TattooCatalog, Integer> {
    TattooCatalog findByname(String name);
}