package com.dette.services;

import java.util.List;

import com.dette.entities.Client;

public interface ClientService {

    void create(Client client);
    List<Client> findAll();
    Client search(String telephone);
    Client searchClientBySurname(String surname);
    Client getClientById(int id);
    
}



