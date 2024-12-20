package com.dette.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.dette.entities.Client;
import com.dette.entities.User;
import com.dette.entities.Article;
import com.dette.entities.Dette;

public class FakeDataLoader {

    private static final List<Dette> dettes = new ArrayList<>();

    public static List<Client> loadClients() {
        List<Client> clients = new ArrayList<>();
        Client client1 = new Client("Amina Ndaw", "771234567", "Dakar");
        clients.add(client1);

        Client client2 = new Client("Babacar Mendy", "775678912", "Thiès");
        clients.add(client2);

        Client clientWithAccount1 = new Client("Khadim Ba", "778912345", "Saint-Louis");
        clientWithAccount1.setUser(new User("khadim.ba", "password123", "Client"));
        clients.add(clientWithAccount1);

        Client clientWithAccount2 = new Client("Dior Sarr", "770987654", "Ziguinchor");
        clientWithAccount2.setUser(new User("dior.sarr", "password456", "Client"));
        clients.add(clientWithAccount2);


        Article article1 = new Article("T-shirt", 10.0, 50);
        Article article2 = new Article("Jeans", 20.0, 30);
        Article article3 = new Article("Veste", 30.0, 20);
        Article article4 = new Article("Chapeau", 5.0, 100);

        Dette dette1 = new Dette(1, LocalDate.now(), 50.0, client1);
        dette1.addArticle("T-shirt", 2);
        dette1.addArticle("Chapeau", 1);
        dettes.add(dette1);

        Dette dette2 = new Dette(2, LocalDate.now(), 120.0, client2);
        dette2.addArticle("Jeans", 3);
        dette2.addArticle("Veste", 1);
        dettes.add(dette2);

        Dette dette3 = new Dette(3, LocalDate.now(), 80.0, clientWithAccount1);
        dette3.addArticle("T-shirt", 4);
        dette3.addArticle("Veste", 2);
        dettes.add(dette3);

        Dette dette4 = new Dette(4, LocalDate.now(), 200.0, clientWithAccount2);
        dette4.addArticle("Jeans", 5);
        dette4.addArticle("Chapeau", 3);
        dettes.add(dette4);

        return clients;
    }

    public static List<Dette> loadDettes() {
        return dettes;
    }
}
