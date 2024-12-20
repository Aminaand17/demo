package com.dette.entities;

import lombok.Data;

@Data
public class User {

    private static int nextId = 1;

    
    private int id;
    private String nom;
    private String prenom;
    private String login;
    private String password;
    private int roleId; 
    private String role; 
    private boolean active = true; 
    
    private Client client;

        public User() {
        }
    
        public User(String nom, String prenom, String login, String password, int roleId) {
            this.id = nextId++; 
            this.nom = nom;
            this.prenom = prenom;
            this.login = login;
            this.password = password;
            this.roleId = roleId;
        }

        public User(String nom, String login, String role) {
            this.id = nextId++; 
            this.nom = nom;
            this.login = login;
            this.role = role;

        }
    
        

    public boolean estAdmin() {
        return "Admin".equals(role);
    }
    
    public boolean estBoutiquier() {
        return "Boutiquier".equals(role);
    }

    public void activer() {
        this.active = true;
    }
    
    public void desactiver() {
        this.active = false;
    }
    
    public boolean estActiver() {
        return active;
    }
}
