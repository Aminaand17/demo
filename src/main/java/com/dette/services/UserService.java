package com.dette.services;

import java.util.List;


import com.dette.entities.User;

public interface UserService {


     void create(User user);
     List<User> findAll();
     User getExistingUser();
     void activateUser(String login) ;
     void desactivateUser(String login) ;
     User findByLogin(String login) ;


}
