package com.dette.auth;

import com.dette.entities.User;
import com.dette.services.UserService;

public class Connexion {

    private UserService userService;

    public Connexion(UserService userService) {
        this.userService = userService;
    }

    public User login(String login, String password) {
        User user = userService.findByLogin(login);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Connexion reussi");
            return user;
        } else {
            System.out.println("Identifiants incorrects");
            return null;
        }
    }
}
