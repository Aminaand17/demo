package com.dette.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.dette.repository.ClientRepository;
import com.dette.core.repository.Repository;
import com.dette.entities.Client;
import com.dette.entities.User;

import com.dette.services.ClientService;

public class ClientServiceImpl implements ClientService {
    private ClientRepository clientRepository;
    private Repository<User> userRepository;

    public ClientServiceImpl(ClientRepository clientRepository, Repository<User> userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void create(Client client) {
        clientRepository.insert(client);

    }

    @Override
    public List<Client> findAll() {
        return clientRepository.selectAll();

    }

    @Override
    public Client search(String telephone) {
        return clientRepository.selectByTelephone(telephone);
    }

    @Override
    public Client searchClientBySurname(String surname) {
        return clientRepository.selectBySurname(surname);
    }

    @Override
    public Client getClientById(int id) {
        return clientRepository.selectById(id);
    }

    

}
