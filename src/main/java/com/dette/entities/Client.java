package com.dette.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode()
public class Client {


    private int id;
    private String surname;
    private String telephone;
    private String adresse;

    private User user;
    private List<Dette> dettes;

    public Client(int id,String surname, String telephone, String adresse) {
        this.id = id;
        this.surname = surname;
        this.telephone = telephone;
        this.adresse = adresse;

    }
    public Client(String surname, String telephone, String adresse) {

        this.surname = surname;
        this.telephone = telephone;
        this.adresse = adresse;

    }

    public Client() {

    }

    public boolean hasAccount() {
        return user != null;
    }

}
