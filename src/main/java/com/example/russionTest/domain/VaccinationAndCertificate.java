package com.example.russionTest.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vaccinationAndCertifficate")
public class VaccinationAndCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date dateVaccination;

    private String reaction;

    @ManyToOne
    @JoinColumn(name = "vaccination_id")
    private Vaccination vaccination;

    @ManyToOne
    @JoinColumn(name = "vaccinationCertifficate_id")
    private VaccinationCertifficate vaccinationCertifficate;
}
