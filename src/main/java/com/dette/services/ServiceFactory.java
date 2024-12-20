package com.dette.services;

import java.util.Scanner;

import com.dette.core.repository.Repository;
import com.dette.entities.Paiement;
import com.dette.entities.User;
import com.dette.repository.ArticleRepository;
import com.dette.repository.ClientRepository;
import com.dette.repository.DetteRepository;
import com.dette.repository.UserRepository;
import com.dette.repository.bd.ClientRepositoryBD;
import com.dette.repository.bd.ArticleRepositoryBD;
import com.dette.repository.bd.UserRepositoryBD;
import com.dette.repository.list.ClientRepositoryList;
import com.dette.repository.bd.DetteRepositoryBD;
import com.dette.repository.bd.PaiementRepositoryBD;
import com.dette.repository.list.DetteRepositoryList;
import com.dette.repository.list.ArticleRepositoryList;
import com.dette.repository.list.PaiementRepository;
import com.dette.repository.list.UserRepositoryList;
import com.dette.services.impl.ArticleServiceImpl;
import com.dette.services.impl.ClientServiceImpl;
import com.dette.services.impl.DetteServiceImpl;

import com.dette.services.impl.UserServiceImpl;

public class ServiceFactory {


    public static Scanner createScanner() {
        return new Scanner(System.in);
    }



    public static UserServiceImpl createUserService() {
        UserRepository userRepository = new UserRepositoryList();
        return new UserServiceImpl(userRepository);
    }

    public static ClientServiceImpl createClientService() {
        ClientRepository clientRepository = new ClientRepositoryList();
        Repository<User> userRepository = new UserRepositoryList(); 
        return new ClientServiceImpl(clientRepository, userRepository);
    }

    public static DetteServiceImpl createDetteService() {
        DetteRepository detteRepository = new DetteRepositoryList();
        return new DetteServiceImpl(detteRepository);
    }



    // toujours en BD
    public static ArticleServiceImpl createArticleService() {
        ArticleRepository articleRepository = new ArticleRepositoryBD();
        return new ArticleServiceImpl(articleRepository);
    }



}


