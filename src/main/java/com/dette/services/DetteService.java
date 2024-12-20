package com.dette.services;

import java.util.List;

import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Dette;
import com.dette.entities.Paiement;

public interface DetteService {

    void create(Dette dette);
    List<Dette> findAll();
    void enregistrerPaiement(Dette dette, double montantPaiement);
    List<Dette> listerDettesNonSoldees();
    List<Dette> listerDettesEnCours();
    void afficherArticlesDette(Dette dette);
    void validerRefuserDette(int idDette, boolean valider);
    Dette createDemandeDette(List<Dette> dettes,double montant); 
    List<Dette> listerDemandesDette(String etatFiltre); 
    void envoyerRelance(Dette dette); 
    List<Article> getArticlesDette(Dette dette); 
    List<Paiement> getPaiementsDette(Dette dette);
    List<Dette> listerDettesNonSoldeesParClient(Client client);


    
}
