package com.example.russionTest.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Period;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vaccinations")
public class Vaccination {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String interval;

    @ManyToOne
    @JoinColumn(name = "vaccinationClass_id")
    private ClassVaccination classVaccination;


    @OneToMany(mappedBy = "vaccination", cascade = CascadeType.ALL)
    private List<VaccinationAndCertificate> vaccinationAndCertifficateList;

}
