package com.example.russionTest.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "record")
public class Records {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String place;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "tattooCatalog_id")
    private TattooCatalog tattooCatalog;

    @ManyToOne
    @JoinColumn(name = "usernameClient", referencedColumnName = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "usernameMaster", referencedColumnName = "username")
    private User master;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public TattooCatalog getTattooCatalog() {
        return tattooCatalog;
    }

    public void setTattooCatalog(TattooCatalog tattooCatalog) {
        this.tattooCatalog = tattooCatalog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }
}
