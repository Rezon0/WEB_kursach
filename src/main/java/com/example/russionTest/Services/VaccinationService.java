package com.example.russionTest.Services;

import com.example.russionTest.domain.User;
import com.example.russionTest.domain.Vaccination;
import com.example.russionTest.domain.VaccinationAndCertificate;
import com.example.russionTest.domain.VaccinationCertifficate;
import com.example.russionTest.repos.UserRepository;
import com.example.russionTest.repos.VacCertRepo;
import com.example.russionTest.repos.VacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VaccinationService {
    @Autowired
    private VacCertRepo vacCertRepo;

    @Autowired
    private VacRepository vacRepository;

    public List<Vaccination> getVaccinationsByCertifficateId(VaccinationCertifficate certifficate) {
//        VaccinationCertifficate certifficate = vacCertRepo.findById(certifficateId).orElse(null);

        if (certifficate == null) {
            return null;
        }

        List<VaccinationAndCertificate> vaccinationAndCertificateList = certifficate.getVaccinationAndCertifficateList();

        List<Vaccination> vaccinations = new ArrayList<>();

        if (vaccinationAndCertificateList != null && !vaccinationAndCertificateList.isEmpty()) {
            for (VaccinationAndCertificate vacAndCert : vaccinationAndCertificateList) {
                Vaccination vaccination = vacAndCert.getVaccination();
                vaccinations.add(vaccination);
            }
        }

        return vaccinations;
    }

    public List<Vaccination> getAllVaccinations(){
        return vacRepository.findAll();
    }
    public  Vaccination getVaccinationByName(String name){
        return vacRepository.findByName(name);
    }
}
