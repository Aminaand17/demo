package com.dette.repository;

import java.util.List;

import com.dette.core.repository.Repository;
import com.dette.entities.Dette;

public interface DetteRepository extends Repository<Dette>{
    void insert(Dette dette, String telephone);
    Dette findById(int id);
    void enregistrerPaiement(Dette dette, double montantPaiement);
    List<Dette> listerDettesNonSoldees();
    List<Dette> listerDettesEnCours();
    void validerRefuserDette(int idDette, boolean valider);
    void afficherArticlesDette(int detteId) ;
    void faireDemandeDette(int clientId, double montant);
    void afficherPaiementsDette(int detteId);
    void listerDettesNonSoldees(int clientId);
    
}