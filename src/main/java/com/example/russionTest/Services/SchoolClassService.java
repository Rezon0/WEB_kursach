package com.example.russionTest.Services;

import com.example.russionTest.domain.SchoolClass;
import com.example.russionTest.domain.User;
import com.example.russionTest.repos.SchoolClassRepo;
import com.example.russionTest.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolClassService {
    @Autowired
    private SchoolClassRepo schoolClassRepo;

    public List<SchoolClass> getAllSchoolClass() {
        return schoolClassRepo.findAll();
    }
}
