package com.dette;

import java.util.Scanner;
import com.dette.entities.User;
import com.dette.repository.ClientRepository;
import com.dette.repository.DetteRepository;
import com.dette.repository.UserRepository;
import com.dette.repository.bd.ClientRepositoryBD;
import com.dette.repository.bd.UserRepositoryBD;
import com.dette.repository.list.ClientRepositoryList;
import com.dette.repository.list.DetteRepositoryList;
import com.dette.repository.list.UserRepositoryList;
import com.dette.services.ClientService;
import com.dette.services.ServiceFactory;
import com.dette.services.UserService;
import com.dette.services.impl.ArticleServiceImpl;
import com.dette.services.impl.ClientServiceImpl;
import com.dette.services.impl.DetteServiceImpl;
import com.dette.services.impl.UserServiceImpl;
import com.dette.view.AdminView;
import com.dette.view.BoutiquierView;
import com.dette.view.ClientView;

public class Main {

    private static ClientService clientServiceImpl;
    private static UserService userServiceImpl;

    public static void main(String[] args) {
        Scanner scanner = ServiceFactory.createScanner();
        ArticleServiceImpl articleService = ServiceFactory.createArticleService();
        userServiceImpl = ServiceFactory.createUserService();
        clientServiceImpl = ServiceFactory.createClientService();
        DetteServiceImpl detteServiceI = ServiceFactory.createDetteService();
        UserRepository userRepository = new UserRepositoryList();

        System.out.print("Veuillez entrer votre nom d'utilisateur : ");
        String login = scanner.nextLine();
        System.out.print("Veuillez entrer votre mot de passe : ");
        String password = scanner.nextLine();
        User user = userRepository.getUserByLogin(login);

        if (user == null) {
            System.out.println("Nom d'utilisateur non trouvé. Veuillez vérifier votre nom d'utilisateur.");
        } else if (!user.getPassword().equals(password)) {
            System.out.println("Mot de passe incorrect. Veuillez réessayer.");
        } else {
            String role = user.getRole();
            System.out.println("Connexion réussie !");
            System.out.println("Rôle de l'utilisateur : " + role);
            afficherMenu(role, scanner, articleService, detteServiceI);
        }
    }

    private static void afficherMenu(String role, Scanner scanner, ArticleServiceImpl articleService,
            DetteServiceImpl detteServiceI) {
        switch (role) {
            case "Admin":
                AdminView adminView = new AdminView(clientServiceImpl);
                adminView.afficherMenuAdmin(scanner, userServiceImpl, articleService, clientServiceImpl, detteServiceI);
                break;
            case "Boutiquier":
                BoutiquierView.afficherMenuBoutiquier(scanner, clientServiceImpl, articleService, detteServiceI);
                break;
            case "Client":
                ClientView.afficherMenuClient(scanner, clientServiceImpl, detteServiceI,articleService);
                break;
            default:
                System.out.println("Rôle inconnu.");
        }
    }
}
