package com.example.russionTest.Services;

import com.example.russionTest.domain.User;
import com.example.russionTest.domain.VaccinationCertifficate;
import com.example.russionTest.repos.UserRepository;
import com.example.russionTest.repos.VacCertRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VacCertService {

    @Autowired
    private VacCertRepo vacCertRepo;

    public VaccinationCertifficate getVacCertByUsername(User user) {
        VaccinationCertifficate vaccinationCertifficate = vacCertRepo.findByUser_Username(user.getUsername());
        if (vaccinationCertifficate == null) throw new UsernameNotFoundException(user.getUsername());
        return vaccinationCertifficate;
    }
}
