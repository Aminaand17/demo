package com.dette.repository.list;

import java.util.List;

import com.dette.entities.Paiement;

public interface PaiementRepository {
    void insert(Paiement paiement, int detteId);
    List<Paiement> findByDetteId(int detteId);
    List<Paiement> findAll();
}

