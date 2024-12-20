package com.dette.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Dette;
import com.dette.entities.Paiement;
import com.dette.entities.User;
import com.dette.services.ClientService;
import com.dette.services.impl.ArticleServiceImpl;
import com.dette.services.impl.DetteServiceImpl;

public class BoutiquierView {
    public static void afficherMenuBoutiquier(Scanner scanner, ClientService clientService,
            ArticleServiceImpl articleService, DetteServiceImpl detteService) {

        int choix;
        Client client;
        do {
            System.out.println("=== Menu Boutiquier ===");
            System.out.println("1. Créer un client");
            System.out.println("2. Lister les clients");
            System.out.println("3. Rechercher client par Téléphone ");
            System.out.println("4. Créer une dette");
            System.out.println("5. Lister les dettes");
            System.out.println("6. Enregistrer un paiement");
            System.out.println("7. Lister les dettes non soldées");
            System.out.println("8. Lister les demandes de dettes en cours");
            System.out.println("9. Voir les articles de la dette");
            System.out.println("10. Valider ou Refuser la dette");
            System.out.println("11. Quitter");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    client = new Client();
                    System.out.println("Entrer le surnom");
                    String surname = scanner.nextLine();
                    if (clientService.searchClientBySurname(surname) == null) {
                        client.setSurname(surname);
                        String phone;
                        while (true) {
                            System.out.println("Entrer le téléphone (9 chiffres)");
                            phone = scanner.nextLine().trim();
                            if (phone.matches("\\d{9}")) { 
                                client.setTelephone(phone);
                                break;
                            } else {
                                System.out.println(
                                        "Numéro de téléphone invalide. Il doit comporter exactement 9 chiffres.");
                            }
                        }

                        System.out.println("Entrer l'adresse");
                        client.setAdresse(scanner.nextLine());
                        System.out.println("Souhaitez-vous créer un compte utilisateur pour ce client ? (oui/non)");
                        String createAccountChoice = scanner.nextLine().trim().toLowerCase();

                        if (createAccountChoice.equals("oui")) {
                            String login;
                            while (true) {
                                System.out.println("Entrer le login pour le compte utilisateur");
                                login = scanner.nextLine().trim();
                                if (!login.isEmpty()) { 
                                    break;
                                } else {
                                    System.out.println("Le login ne peut pas être vide.");
                                }
                            }

                            String password;
                            while (true) {
                                System.out.println("Entrer le mot de passe pour le compte utilisateur");
                                password = scanner.nextLine().trim();
                                if (password.length() >= 6) { 
                                    break;
                                } else {
                                    System.out.println("Le mot de passe doit comporter au moins 6 caractères.");
                                }
                            }
                            User user = new User();
                            user.setLogin(login);
                            user.setPassword(password);
                            user.setRole("Client"); 

                            client.setUser(user);
                        }

                        clientService.create(client); 
                    } else {
                        System.out.println("Le surnom existe déjà");
                    }
                    break;

                case 2:
                    List<Client> list = clientService.findAll();
                    list.forEach(System.out::println);
                    break;
                case 3:
                    System.out.println("Entrer le Téléphone");
                    String tel = scanner.nextLine();
                    client = clientService.search(tel);
                    if (client == null) {
                        System.out.println("Client non trouvé");
                    } else {
                        System.out.println(client);
                    }
                    break;
                case 4:
                    System.out.println("Entrer l'ID de la dette :");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    Dette detteExistante = detteService.findById(id);
                    if (detteExistante != null) {
                        System.out.println("L'ID de la dette existe déjà.");
                        break;
                    }

                    System.out.println("Entrer le téléphone du client :");
                    String telephone = scanner.nextLine();
                    Client clientD = clientService.search(telephone);

                    if (clientD == null) {
                        System.out.println("Client introuvable.");
                    } else {
                        System.out.println("Entrer le montant total de la dette :");
                        double montant = scanner.nextDouble();
                        scanner.nextLine();
                        if (montant <= 0) {
                            System.out.println("Le montant doit être un nombre positif.");
                            break;
                        }
                        Dette dette = new Dette(id, LocalDate.now(), montant);
                        dette.setClient(clientD);

                        System.out.println("Ajouter des articles à la dette (entrez F pour terminer) :");
                        while (true) {
                            System.out.println("Entrer le nom de l'article : ");
                            String nomArticle = scanner.nextLine();
                            if ("F".equalsIgnoreCase(nomArticle)) {
                                break;
                            }
                            System.out.println("Entrer la quantité de cet article : ");
                            int qte = scanner.nextInt();
                            scanner.nextLine();
                            if (qte <= 0) {
                                System.out.println("La quantité doit être un nombre positif.");
                                continue;
                            }
                            dette.addArticle(nomArticle, qte);
                        }
                        detteService.create(dette);
                        System.out.println("Dette créée pour le client : " + clientD.getSurname());
                    }
                    // System.out.println("Entrer le téléphone du client :");
                    // String telephone = scanner.nextLine();
                    // Client clientD = clientService.search(telephone);
    
                    // if (clientD != null) {
                    //     System.out.println("Liste des articles disponibles : ");
                    //     List<Article> articlesDisponibles = articleService.findAll(); 
                    //     for (Article article : articlesDisponibles) {
                    //         System.out.println(article.getNom() + " - " + article.getPrix() + " € (Stock: " + article.getQteStock() + ")");
                    //     }
    
                    //     double montantTotal = 0;
                    //     Dette dette = new Dette(0, LocalDate.now(), montantTotal, clientD); 
    
                    //     System.out.println("Ajouter des articles à la dette (entrez F pour terminer) :");
                    //     while (true) {
                    //         System.out.println("Entrez le nom de l'article à ajouter (ou F pour terminer) :");
                    //         String nomArticle = scanner.nextLine();
    
                    //         if (nomArticle.equalsIgnoreCase("F")) {
                    //             break;
                    //         }
                    //         Article article = articleService.getArticleByNom(nomArticle);
                    //         if (article != null) {
                    //             System.out.println("Entrer la quantité de cet article : ");
                    //             int qte = scanner.nextInt();
                    //             scanner.nextLine(); 
                    //             if (qte > article.getQteStock()) {
                    //                 System.out.println("Quantité demandée supérieure au stock disponible.");
                    //                 continue;
                    //             }
                    //             double prixTotal = article.getPrix() * qte;
                    //             montantTotal += prixTotal;
                    //             dette.addArticle(nomArticle, qte);
                    //         } else {
                    //             System.out.println("Article non trouvé.");
                    //         }
                    //     }
                    //     dette.setMontantRestant(montantTotal);
                    //     detteService.create(dette);
                    //     System.out.println("Demande de dette créée avec un montant total de : " + montantTotal + " €");
                    // } else {
                    //     System.out.println("Client non trouvé.");
                    // }
                    break;
        

                case 5:
                    List<Dette> listD = detteService.findAll();
                    listD.forEach(System.out::println);
                    break;

                case 6:
                    System.out.println("Entrer l'ID de la dette pour enregistrer un paiement :");
                    int idDette = scanner.nextInt();
                    scanner.nextLine();
                    Dette detteToPay = detteService.findById(idDette);
                    if (detteToPay == null) {
                        System.out.println("Dette introuvable.");
                    } else {
                        System.out.println("Entrer le montant du paiement :");
                        double montantPaiement = scanner.nextDouble();
                        scanner.nextLine();
                        if (montantPaiement <= 0) {
                            System.out.println("Le montant du paiement doit être positif.");
                        } else if (montantPaiement > detteToPay.getMontantRestant()) {
                            System.out.println(
                                    "Le montant du paiement ne peut pas être supérieur au montant restant de la dette.");
                        } else {
                            System.out.println("Enregistrement du paiement...");
                            detteService.enregistrerPaiement(detteToPay, montantPaiement);
                            if (detteToPay.getMontantRestant() == 0) {
                                System.out.println("La dette est maintenant soldée.");
                            } else {
                                System.out.println("Paiement enregistré. Montant restant à payer : "
                                        + detteToPay.getMontantRestant());
                            }
                        }
                    }
                    break;

                case 7:
                    System.out.println("Dettes non soldées : ");
                    List<Dette> dettesNonSoldees = detteService.listerDettesNonSoldees();
                    for (Dette detteNonSoldee : dettesNonSoldees) {
                        System.out.println(detteNonSoldee);
                    }
                    break;
                case 8:
                    System.out.println("Demandes de dettes en cours : ");
                    List<Dette> dettesEnCours = detteService.listerDettesEnCours();
                    for (Dette detteEnCours : dettesEnCours) {
                        System.out.println(detteEnCours);
                    }
                    break;
                case 9:
                    System.out.println("Entrer l'ID de la dette pour voir les articles :");
                    int idDetteArticle = scanner.nextInt();
                    scanner.nextLine();

                    List<Dette> dettesA = detteService.findAll();
                    if (dettesA.isEmpty()) {
                        System.out.println("Aucune dette disponible.");
                    } else {
                        if (idDetteArticle > 0 && idDetteArticle <= dettesA.size()) {
                            Dette detteArticle = dettesA.get(idDetteArticle - 1);
                            detteService.afficherArticlesDette(detteArticle);
                        } else {
                            System.out.println("Erreur : L'ID de la dette est invalide.");
                        }
                    }
                    break;
                case 10:
                    System.out.println("Entrer l'ID de la dette pour valider ou refuser :");
                    int idDetteV = scanner.nextInt();
                    scanner.nextLine();
                    Dette detteToValidate = detteService.findById(idDetteV);
                    if (detteToValidate == null) {
                        System.out.println("Dette introuvable.");
                    } else {
                        System.out.println("Voulez-vous valider la dette ? (oui/non)");
                        String decision = scanner.nextLine().trim().toLowerCase();
                        boolean isValid = decision.equals("oui");
                        detteService.validerRefuserDette(idDetteV, isValid);
                        System.out.println("Dette " + (isValid ? "validée" : "refusée"));
                    }
                    break;
                case 11:
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide, veuillez essayer de nouveau.");
            }
        } while (choix != 11);
    }
}
