package com.example.russionTest.repos;

import com.example.russionTest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByusername(String username);

    User findByLastnameAndAndFirstnameAndPatronymicAndBirthdayAndSchoolClassName(String lastname, String firstname, String patronymic, Date birthday, String schoolClassName);


}
