package com.dette.repository;

import com.dette.core.repository.Repository;
import com.dette.entities.Client;

public interface ClientRepository extends Repository<Client>{
     Client selectByTelephone(String telephone);
     Client selectBySurname(String surname);
     Client selectById(int id);

    
} 
