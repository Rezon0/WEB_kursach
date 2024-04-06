package com.example.russionTest.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Duration;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "med_institute")
public class MedInstitute {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String address;

    @OneToMany(mappedBy = "medInstitute", cascade = CascadeType.ALL)
    private List<VaccinationCertifficate> vaccinationCertifficateList;
}
