package com.example.russionTest.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.sql.Date;
import java.time.Duration;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vaccination_certifficates")
public class VaccinationCertifficate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date dateGive;

    private byte groupBlood;

    private boolean rhesus;

    @ManyToOne
    @JoinColumn(name = "medInstitute_id")
    private MedInstitute medInstitute;

    @OneToMany(mappedBy = "vaccinationCertifficate", cascade = CascadeType.ALL)
    private List<VaccinationAndCertificate> vaccinationAndCertifficateList;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateGive() {
        return dateGive;
    }

    public void setDateGive(Date dateGive) {
        this.dateGive = dateGive;
    }

    public byte getGroupBlood() {
        return groupBlood;
    }

    public void setGroupBlood(byte groupBlood) {
        this.groupBlood = groupBlood;
    }

    public boolean isRhesus() {
        return rhesus;
    }

    public void setRhesus(boolean rhesus) {
        this.rhesus = rhesus;
    }

    public MedInstitute getMedInstitute() {
        return medInstitute;
    }

    public void setMedInstitute(MedInstitute medInstitute) {
        this.medInstitute = medInstitute;
    }

    public List<VaccinationAndCertificate> getVaccinationAndCertifficateList() {
        return vaccinationAndCertifficateList;
    }

    public void setVaccinationAndCertifficateList(List<VaccinationAndCertificate> vaccinationAndCertifficateList) {
        this.vaccinationAndCertifficateList = vaccinationAndCertifficateList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
