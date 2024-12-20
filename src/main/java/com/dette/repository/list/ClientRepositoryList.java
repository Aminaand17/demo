package com.dette.repository.list;

import com.dette.entities.Client;
import com.dette.repository.ClientRepository;
import com.dette.services.FakeDataLoader;

public class ClientRepositoryList extends RepositoryListImpl<Client> implements ClientRepository {

    public ClientRepositoryList() {
        super(); 
        list.addAll(FakeDataLoader.loadClients());
    }

    @Override
    public Client selectByTelephone(String telephone) {
        return list.stream()
                   .filter(client -> client.getTelephone().equals(telephone))
                   .findFirst()
                   .orElse(null);
    }

    @Override
    public Client selectBySurname(String surname) {
        return list.stream()
                   .filter(client -> client.getSurname().equals(surname))
                   .findFirst()
                   .orElse(null);
    }

    @Override
    public Client selectById(int id) {
        return list.stream()
                   .filter(client -> client.getId() == id)
                   .findFirst()
                   .orElse(null);
    }
}
