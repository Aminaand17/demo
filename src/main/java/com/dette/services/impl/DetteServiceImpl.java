package com.dette.services.impl;
import java.util.ArrayList;
import java.util.List;
import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Dette;
import com.dette.entities.Paiement;
import com.dette.repository.DetteRepository;
import com.dette.services.DetteService;
import com.dette.services.FakeDataLoader;

import java.time.LocalDate;

public class DetteServiceImpl implements DetteService {
    private DetteRepository detteRepository ;


   

    public DetteServiceImpl(DetteRepository detteRepository) {
        this.detteRepository = detteRepository;
    }

    @Override
    public void create(Dette dette) {
        detteRepository.insert(dette);
    }

    @Override
    public List<Dette> findAll() {
        return detteRepository.selectAll();
    }

    @Override
    public void enregistrerPaiement(Dette dette, double montantPaiement) {
        if (montantPaiement <= dette.getMontantRestant()) {
            Paiement paiement = new Paiement(LocalDate.now(), montantPaiement);
            dette.addPaiement(paiement);
        } else {
            System.out.println("Le montant dépasse le montant restant de la dette.");
        }
    }

    public List<Dette> listerDettesNonSoldees() {
        List<Dette> dettesNonSoldees = new ArrayList<>();
        for (Dette dette : findAll()) { 
            if (dette.getMontantRestant() > 0) {
                dettesNonSoldees.add(dette);
            }
        }
        return dettesNonSoldees;
    }

    public List<Dette> listerDettesEnCours() {
        List<Dette> dettesEnCours = new ArrayList<>();
        for (Dette dette : findAll()) {
            if (dette.getStatut().equals("EN COURS")) {
                dettesEnCours.add(dette);
            }
        }
        return dettesEnCours;
    }

    public void afficherArticlesDette(Dette dette) {
        System.out.println("Articles pour la dette sélectionnée : ");
        for (Article article : dette.getArticles()) {
            System.out.println(article.getNom() + " - Quantité: " + article.getQteStock());
        }
    }

    public void validerRefuserDette(int idDette, boolean valider) {
        List<Dette> dettes = findAll();
        if (idDette > 0 && idDette <= dettes.size()) {
            Dette dette = dettes.get(idDette - 1);
            if (valider) {
                dette.setStatut("VALIDÉE");
                System.out.println("Dette validée : " + dette);
            } else {
                dette.setStatut("REFUSÉE");
                System.out.println("Dette refusée : " + dette);
            }
        } else {
            System.out.println("Erreur : ID de la dette invalide.");
        }
    }

    public Dette createDemandeDette(List<Dette> dettes, double montant) {
        Dette nouvelleDette = new Dette(dettes.size() + 1, LocalDate.now(), montant);
        nouvelleDette.setStatut("EN COURS");
        dettes.add(nouvelleDette);
        return nouvelleDette;
    }

    public List<Dette> listerDemandesDette(String etatFiltre) {
        List<Dette> demandesFiltrees = new ArrayList<>();
        for (Dette dette : demandesFiltrees) {
            if (etatFiltre.equalsIgnoreCase("tout") || dette.getStatut().equalsIgnoreCase(etatFiltre)) {
                demandesFiltrees.add(dette);
            }
        }
        return demandesFiltrees;
    }

    public void envoyerRelance(Dette dette) {
        if ("ANNULER".equalsIgnoreCase(dette.getStatut())) {
            System.out.println("Relance envoyée pour la dette annulée avec ID : " + dette.getId());
            dette.setStatut("EN COURS");
        } else {
            System.out.println("Impossible de relancer cette dette car elle n'est pas annulée.");
        }
    }


    public List<Article> getArticlesDette(Dette dette) {
        return dette.getArticles(); 
    }

    public List<Paiement> getPaiementsDette(Dette dette) {
        return dette.getPaiements(); 
    }

    public Dette findById(int id) {
        return detteRepository.findById(id);
    }


    public List<Dette> listerDettesParClient(Client client) {
        List<Dette> dettesClient = new ArrayList<>();
        for (Dette dette : findAll()) {
            if (dette.getClient().equals(client)) {
                dettesClient.add(dette);
            }
        }
        return dettesClient;
    }

    
    public List<Dette> listerDettesNonSoldeesParClient(Client client) {
        List<Dette> dettesNonSoldees = new ArrayList<>();
        for (Dette dette : listerDettesParClient(client)) {
            if (dette.getMontantRestant() > 0) {
                dettesNonSoldees.add(dette);
            }
        }
        return dettesNonSoldees;
    }


    

   
    
}


