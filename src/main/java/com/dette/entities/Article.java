package com.dette.entities;

import java.time.LocalDate;
import java.util.ArrayList;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode()

public class Article {
    private String nom;
    private String reference;
    private int qteStock;
    private double prix;


    public Article(String nom, double prix, int qteStock) {
        this.nom = nom;
        this.prix = prix;
        this.qteStock = qteStock;
    }

    public Article() {

    }

    
    public boolean isDisponible() {
        return qteStock > 0;
    }
}
