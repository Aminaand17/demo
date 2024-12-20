package com.dette.view;

import java.util.List;
import java.util.Scanner;

import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.User;
import com.dette.services.ClientService;
import com.dette.services.ServiceFactory;
import com.dette.services.UserService;
import com.dette.services.impl.ArticleServiceImpl;
import com.dette.services.impl.DetteServiceImpl;

public class AdminView {

    private ClientService clientService;

    public AdminView(ClientService clientService) {
        this.clientService = clientService;
    }

    public  void afficherMenuAdmin(Scanner scanner, UserService userService, ArticleServiceImpl articleService,
            ClientService clientService, DetteServiceImpl detteService) {

        int choix;
        Client client;
        User user;
        String tel;

        do {
            System.out.println("=== Menu Admin ===");
            System.out.println("1. Creer un client");
            System.out.println("2. Lister les clients");
            System.out.println("3. Créer un compte à un client n'ayant pas de compte");
            System.out.println("4. Créer un compte utilisateur avec un rôle (Admin ou Boutiquier)");
            System.out.println("5. Activer un compte utilisateur");
            System.out.println("6. Désactiver un compte utilisateur");
            System.out.println("7. Afficher les comptes utilisateurs actifs ou par rôle");
            System.out.println("8. Créer des articles");
            System.out.println("9. Lister les articles");
            System.out.println("10. Filtrer les articles par disponibilité (qteStock != 0)");
            System.out.println("11. Mettre à jour la quantité en stock d’un article");
            System.out.println("12. Archiver les dettes soldées");
            System.out.println("13. Quitter");
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

                            User userL = new User();
                            userL.setLogin(login);
                            userL.setPassword(password);
                            userL.setRole("Client");

                            client.setUser(userL); 
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
                    System.out.println("Entrer le téléphone du client");
                    tel = scanner.nextLine();
                    client = clientService.search(tel);

                    if (client == null) {
                        System.out.println("Client non trouvé");
                    } else if (client.hasAccount()) {
                        System.out.println("Le client a déjà un compte");
                    } else {
                        user = new User();
                        System.out.println("Entrer le login");
                        user.setLogin(scanner.nextLine());
                        System.out.println("Entrer le mot de passe");
                        user.setPassword(scanner.nextLine());
                        client.setUser(user);
                        System.out.println("Compte utilisateur créé pour le client " + client.getSurname());
                    }
                    break;
                case 4:
                    user = new User();
                    System.out.println("Entrer le Nom");
                    user.setNom(scanner.nextLine());
                    System.out.println("Entrer le Prenom");
                    user.setPrenom(scanner.nextLine());
                    System.out.println("Entrer le login");
                    user.setLogin(scanner.nextLine());
                    System.out.println("Entrer le mot de passe");
                    user.setPassword(scanner.nextLine());
                    System.out.println("Entrer le rôle (Admin/Boutiquier)");
                    String role = scanner.nextLine();
                    if ("Admin".equals(role) || "Boutiquier".equals(role)) {
                        user.setRole(role);
                        System.out.println("Compte utilisateur avec rôle " + role + " créé.");

                        userService.create(user);
                    } else {
                        System.out.println("Rôle incorrect");
                    }
                    break;
                case 5:
                    System.out.println("Entrer le login de l'utilisateur à activer");
                    String loginActiver = scanner.nextLine();
                    User userActiver = userService.findByLogin(loginActiver);
                    if (userActiver != null) {
                        userService.activateUser(loginActiver);
                        System.out.println("Compte activé : " + userActiver);
                    } else {
                        System.out.println("Utilisateur non trouvé");
                    }
                    break;
                case 6:
                    System.out.println("Entrer le login de l'utilisateur à désactiver");
                    String loginDesactiver = scanner.nextLine().trim(); 
                    User userDesactiver = userService.findByLogin(loginDesactiver);

                    if (userDesactiver != null) {
                        userService.desactivateUser(loginDesactiver);
                        System.out.println("Compte désactivé : " + userDesactiver);
                    } else {
                        System.out.println("Utilisateur non trouvé");
                    }
                    break;

                    case 7:
                    System.out.println("1. Afficher les comptes actifs");
                    System.out.println("2. Afficher par rôle (Admin/Boutiquier)");
                    int subChoix = scanner.nextInt();
                    scanner.nextLine(); 
                
                    if (subChoix == 1) {
                        System.out.println("Affichage des comptes actifs :");
                        userService.findAll()
                                .stream()
                                .filter(u -> u.isActive())
                                .forEach(u -> System.out.println(u));
                    } else if (subChoix == 2) {
                        System.out.println("Entrer le rôle (Admin/Boutiquier) :");
                        String roleR = scanner.nextLine().trim();
                        System.out.println("Filtrage des utilisateurs par rôle (" + roleR + ") :");
                        userService.findAll()
                                .stream()
                                .filter(u -> u.getRole() != null && roleR.equalsIgnoreCase(u.getRole()))
                                .forEach(u -> System.out.println(u)); 
                    } else {
                        System.out.println("Choix invalide.");
                    }
                    break;
                
                case 8:
                    Article article = new Article();
                    System.out.println("Entrer le nom de l'article");
                    article.setNom(scanner.nextLine());
                    System.out.println("Entrer la quantité en stock");
                    article.setQteStock(scanner.nextInt());
                    System.out.println("Entrer le prix de l'article");
                    article.setPrix(scanner.nextDouble());
                    articleService.create(article);
                    break;

                case 9:
                    articleService.findAll().forEach(System.out::println);
                    break;
                case 10:
                    articleService.findAll().stream()
                            .filter(Article::isDisponible)
                            .forEach(System.out::println);
                    break;
                case 11:
                    System.out.println("Entrer le nom de l'article à mettre à jour");
                    String nomArticle = scanner.nextLine();
                    System.out.println("Entrer la nouvelle quantité");
                    int qte = scanner.nextInt();
                    articleService.updateStock(nomArticle, qte);
                    break;
                case 12:
                    break;
                default:
                    System.out.println("Choix invalide.");
                    break;
            }
        } while (choix != 13);
    }
}
