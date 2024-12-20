package com.dette.repository;

import java.util.List;

import com.dette.core.repository.Repository;
import com.dette.entities.User;

public interface UserRepository extends Repository<User>{
     User selectByLogin(String login);
     User getUserByLogin(String login);
     void activateUser(String login);
     void desactivateUser(String login);
     List<User> selectByRole(String roleName);
     void createAccountForClientByTelephone(String telephone, String login, String password) ;
     List<User> selectActiveUsers();
     List<User> selectUsersByRole(String role);

    
} 
