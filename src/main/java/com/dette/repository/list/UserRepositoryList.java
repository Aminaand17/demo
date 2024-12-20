package com.dette.repository.list;

import java.util.List;

import com.dette.entities.User;
import com.dette.entities.UserFactory;
import com.dette.repository.UserRepository;

public class UserRepositoryList extends RepositoryListImpl<User> implements UserRepository{


    public UserRepositoryList() {

        List<User> defaultUsers = UserFactory.createDefaultUsers();
        list.addAll(defaultUsers);

    }



    @Override
    public User getUserByLogin(String login) {
        return list.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }

    
   
    @Override
    public User selectByLogin(String login) {
        return list.stream()
                    .filter(user -> user.getLogin() != null && user.getLogin().equalsIgnoreCase(login.trim()))  // Comparaison insensible à la casse et suppression des espaces
                    .findFirst()
                    .orElse(null);
    }
    


    // public User getUserByLogin(String login) {
    //     return list.stream()
    //         .filter(user -> user.getLogin().equals(login))
    //         .findFirst()
    //         .orElse(null);
    // }

    @Override
    public void activateUser(String login) {
        User user = selectByLogin(login);
        if (user != null) {
            user.activer();
        }
    }

    @Override
    public void desactivateUser(String login) {
        User user = selectByLogin(login);
        if (user != null) {
            user.desactiver();
        }
    }



    @Override
    public List<User> selectByRole(String roleName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectByRole'");
    }



    @Override
    public void createAccountForClientByTelephone(String telephone, String login, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccountForClientByTelephone'");
    }



    @Override
    public List<User> selectActiveUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectActiveUsers'");
    }



    @Override
    public List<User> selectUsersByRole(String role) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectUsersByRole'");
    }
}
