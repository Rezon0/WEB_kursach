package com.example.russionTest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public class VaccinationCalendar {
    private String lastname;
    private String firstname;
    private String patronymic;
    private int vacCertId;
    private LocalDate birthday;
    private String schoolClassName;
    private String vacClassName;
    private String vacName;
    private LocalDate datePlanVaccine;
    private LocalDate dateFactVaccine;
    private String reaction;
    private String highlighted;

    public int getVacCertId() {
        return vacCertId;
    }

    public void setVacCertId(int vacCertId) {
        this.vacCertId = vacCertId;
    }

    public String getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(String highlighted) {
        this.highlighted = highlighted;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getSchoolClassName() {
        return schoolClassName;
    }

    public void setSchoolClassName(String schoolClassName) {
        this.schoolClassName = schoolClassName;
    }

    public String getVacClassName() {
        return vacClassName;
    }

    public void setVacClassName(String vacClassName) {
        this.vacClassName = vacClassName;
    }

    public String getVacName() {
        return vacName;
    }

    public void setVacName(String vacName) {
        this.vacName = vacName;
    }

    public LocalDate getDatePlanVaccine() {
        return datePlanVaccine;
    }

    public void setDatePlanVaccine(LocalDate datePlanVaccine) {
        this.datePlanVaccine = datePlanVaccine;
    }

    public LocalDate getDateFactVaccine() {
        return dateFactVaccine;
    }

    public void setDateFactVaccine(LocalDate dateFactVaccine) {
        this.dateFactVaccine = dateFactVaccine;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
}