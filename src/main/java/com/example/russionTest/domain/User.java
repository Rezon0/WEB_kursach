package com.example.russionTest.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private String username;

    private String password;

    private boolean enabled;

    private String firstname;

    private String lastname;

    private String patronymic;

    private String gender;

    private String phone;

    private String email;

    private String url;

    private Date birthday;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }


    @OneToOne(mappedBy = "userRole", cascade = CascadeType.ALL)
    private Authority authority;

    @ManyToOne
    @JoinColumn(name = "schoolClass_id")
    private SchoolClass schoolClass;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }
}
