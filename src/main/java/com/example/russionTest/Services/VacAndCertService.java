package com.example.russionTest.Services;

import com.example.russionTest.domain.Vaccination;
import com.example.russionTest.domain.VaccinationAndCertificate;
import com.example.russionTest.domain.VaccinationCertifficate;
import com.example.russionTest.repos.VaccinationAndCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class VacAndCertService {

    @Autowired
    private VaccinationAndCertificateRepository vaccinationAndCertificateRepository;

    public VaccinationAndCertificate getVaccinationAndCertificateByVaccinationIdAndVaccinationCertifficateId(int vaccinationId, int vaccinationCertifficateId) {
        return vaccinationAndCertificateRepository.findByVaccinationIdAndVaccinationCertifficateId(vaccinationId, vaccinationCertifficateId);
    }

    public List<VaccinationAndCertificate> getAllVaC(){
        return vaccinationAndCertificateRepository.findAll();
    }

    public void saveVaccinationAndCertificate(Vaccination vaccination, VaccinationCertifficate vaccinationCertifficate, String dateVaccination, String reaction){
        VaccinationAndCertificate VaC = vaccinationAndCertificateRepository.findByVaccinationIdAndVaccinationCertifficateId(vaccination.getId(), vaccinationCertifficate.getId());

        System.out.println(getAllVaC().size() + " jlaksjldkajsld");

        if(VaC == null){
            VaC = new VaccinationAndCertificate();
            VaC.setId(getAllVaC().size()+1);
            VaC.setVaccination(vaccination);
            VaC.setVaccinationCertifficate(vaccinationCertifficate);
        }

        VaC.setDateVaccination(Date.valueOf(dateVaccination));
        VaC.setReaction(reaction);
        vaccinationAndCertificateRepository.save(VaC);
    }
}