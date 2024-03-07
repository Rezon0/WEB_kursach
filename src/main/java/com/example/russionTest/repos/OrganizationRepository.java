package com.example.russionTest.repos;

import com.example.russionTest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrganizationRepository extends
        JpaRepository<User,Integer>,
        JpaSpecificationExecutor<User> {
}
