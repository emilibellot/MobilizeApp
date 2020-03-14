package com.app.mobilize;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuari {
    String username;
    String password;
    String email;
    String gender;
    int height;
    Double weight;
    String dateNaixement;

    public Usuari (){};

    public Usuari (String username, String password, String email ) {
        this.username = username;
        this.password = password;
        this.email = email;
        gender = "";
        height = 0;
        weight = 0.0;
        dateNaixement = "mm/dd/aaaa";
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDateNaixement() {
        return dateNaixement;
    }

    public void setDateNaixement(String dateNaixement) {
        this.dateNaixement = dateNaixement;
    }

    @Override
    public String toString() {
        return username;
    }
}
