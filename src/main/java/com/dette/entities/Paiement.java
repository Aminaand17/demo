package com.dette.entities;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Paiement {
    private LocalDate datePaiement;
    private double montantPaiement;


    public Paiement(LocalDate datePaiement, double montantPaiement) {
        this.datePaiement = datePaiement;
        this.montantPaiement = montantPaiement;
    }
}
