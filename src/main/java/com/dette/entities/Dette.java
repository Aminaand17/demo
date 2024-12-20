package com.dette.entities;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import lombok.Data;

@Data

public class Dette {
    private int id;
    private LocalDate date;               
    private double montant;               
    private double montantVerser;         
    private double montantRestant;          
    private List<Article> articles; 
    private List<Paiement> paiements = new ArrayList<>(); 
    private String statut;
    private int clientId; 
    private Client client;     
 

    public Dette(int id, LocalDate date, double montant) {
        this.id = id;
        this.date = date; 
        this.montant = montant; 
        this.montantVerser = 0; 
        this.montantRestant = montant; 
        this.articles = new ArrayList<>();
        this.paiements = new ArrayList<>();
        this.statut = "EN COURS";
    }


    public Dette(int id, LocalDate date, double montant, Client client) {
        this.id = id;
        this.date = date; 
        this.montant = montant; 
        this.montantVerser = 0; 
        this.montantRestant = montant; 
        this.articles = new ArrayList<>();
        this.paiements = new ArrayList<>();
        this.statut = "EN COURS";
        this.client = client; 
    }

    // public void addArticle(Article article) {
    //     articles.add(article);
    //     montantRestant -= article.getPrix();
    // }


    public void addArticle(String nom, int quantite) {
        Article article = new Article();
        article.setNom(nom);
        article.setQteStock(quantite); 

    
        articles.add(article); 

    }

  
    public void addArticle(Article article, int quantity) {
    }
    

    public void addPaiement(Paiement paiement) {
        paiements.add(paiement);
        montantVerser += paiement.getMontantPaiement();
        montantRestant = montant - montantVerser;
    
        if (montantRestant == 0) {
            System.out.println("La dette est maintenant soldée.");
        }
    }

    public int getClientId() {
        if (client != null) {
            return client.getId();
        }
        return 0; 
    }
}