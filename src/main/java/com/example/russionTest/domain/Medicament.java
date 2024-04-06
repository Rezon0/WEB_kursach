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
@Table(name = "medicament")
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
}
