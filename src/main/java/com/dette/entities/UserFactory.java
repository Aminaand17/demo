package com.dette.entities;

import java.util.ArrayList;
import java.util.List;

public class UserFactory {
    public static List<User> createDefaultUsers() {
        List<User> users = new ArrayList<>();

        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword("admin123");
        admin.setRole("Admin");
        users.add(admin);

        User boutiquier = new User();
        boutiquier.setLogin("boutiquier");
        boutiquier.setPassword("boutiquier123");
        boutiquier.setRole("Boutiquier");
        users.add(boutiquier);

        User client = new User();
        client.setLogin("client");
        client.setPassword("client123");
        client.setRole("Client");
        users.add(client);

        return users;
    }
}

