package com.dette.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import com.dette.entities.Dette;
import com.dette.entities.Client;
import com.dette.entities.Article;
import com.dette.services.ClientService;
import com.dette.services.DetteService;
import com.dette.services.ArticleService;
import com.dette.services.impl.DetteServiceImpl;
import com.dette.services.impl.ArticleServiceImpl;

public class ClientView {

    public static void afficherMenuClient(Scanner scanner, ClientService clientService, DetteServiceImpl detteService, ArticleService articleService) {
        int choix;
        Client client;
        do {
            System.out.println("=== Menu Client ===");
            System.out.println("1. Lister mes dettes non soldées");
            System.out.println("2. Créer une demande de dette");
            System.out.println("3. Lister mes demandes de dette");
            System.out.println("4. Envoyer une relance pour une demande annulée");
            System.out.println("5. Quitter");
            choix = scanner.nextInt();
            scanner.nextLine();
    
            switch (choix) {
                case 1:
                    System.out.println("Entrer votre numéro de téléphone pour lister vos dettes non soldées :");
                    String telephone = scanner.nextLine();
                    client = clientService.search(telephone);
    
                    if (client != null) {
                        System.out.println("Liste des dettes non soldées : ");
                        List<Dette> dettesNonSoldees = detteService.listerDettesNonSoldeesParClient(client);
                        for (Dette dette : dettesNonSoldees) {
                            System.out.println(dette);
                        }
                    } else {
                        System.out.println("Client non trouvé.");
                    }
                    break;
    
                case 2:
                    System.out.println("Entrer votre numéro de téléphone pour créer une demande de dette :");
                    String telephoneForDemande = scanner.nextLine();
                    client = clientService.search(telephoneForDemande);
    
                    if (client != null) {
                        System.out.println("Liste des articles disponibles : ");
                        List<Article> articlesDisponibles = articleService.findAll(); 
                        for (Article article : articlesDisponibles) {
                            System.out.println(article.getNom() + " - " + article.getPrix() + " € (Stock: " + article.getQteStock() + ")");
                        }
    
                        double montantTotal = 0;
                        Dette dette = new Dette(0, LocalDate.now(), montantTotal, client); 
    
                        System.out.println("Ajouter des articles à la dette (entrez F pour terminer) :");
                        while (true) {
                            System.out.println("Entrez le nom de l'article à ajouter (ou F pour terminer) :");
                            String nomArticle = scanner.nextLine();
    
                            if (nomArticle.equalsIgnoreCase("F")) {
                                break;
                            }
                            Article article = articleService.getArticleByNom(nomArticle);
                            if (article != null) {
                                System.out.println("Entrer la quantité de cet article : ");
                                int qte = scanner.nextInt();
                                scanner.nextLine(); 
                                if (qte > article.getQteStock()) {
                                    System.out.println("Quantité demandée supérieure au stock disponible.");
                                    continue;
                                }
                                double prixTotal = article.getPrix() * qte;
                                montantTotal += prixTotal;
                                dette.addArticle(nomArticle, qte);
                            } else {
                                System.out.println("Article non trouvé.");
                            }
                        }
                        dette.setMontantRestant(montantTotal);
                        detteService.create(dette);
                        System.out.println("Demande de dette créée avec un montant total de : " + montantTotal + " €");
                    } else {
                        System.out.println("Client non trouvé.");
                    }
                    break;
    
                case 3:
                    System.out.println("Entrer votre numéro de téléphone pour lister vos demandes de dette :");
                    String telephoneForListe = scanner.nextLine();
                    client = clientService.search(telephoneForListe); 
    
                    if (client != null) {
                        List<Dette> demandesDettes = detteService.listerDettesParClient(client);
                        System.out.println("Demandes de dette : ");
                        for (Dette d : demandesDettes) {
                            System.out.println(d);
                        }
                    } else {
                        System.out.println("Client non trouvé.");
                    }
                    break;
    
                case 4:
                    System.out.println("Entrer l'ID de la demande annulée pour relance :");
                    int idDetteRelance = scanner.nextInt();
                    scanner.nextLine();
                    Dette detteRelance = detteService.findById(idDetteRelance);
                    if (detteRelance != null) {
                        System.out.println("Relance envoyée pour la dette avec ID: " + idDetteRelance);
                    } else {
                        System.out.println("Dette non trouvée.");
                    }
                    break;
    
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        } while (choix != 5);
    }
}
