package com.dette.services.impl;

import java.util.List;
import com.dette.entities.User;
import com.dette.repository.UserRepository;
import com.dette.services.UserService;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        userRepository.insert(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.selectAll();
    }

    @Override
    public void activateUser(String login) {
        userRepository.activateUser(login);
    }

    @Override
    public void desactivateUser(String login) {
        userRepository.desactivateUser(login);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.selectByLogin(login);
    }


    public User getExistingUser() {
        List<User> existingUsers = findAll(); 
        if (!existingUsers.isEmpty()) {
            return existingUsers.get(0);
        }
        return null;
    }
}

// package com.dette.services.impl;
// import java.util.List;

// import com.dette.repository.Repository;
// import com.dette.repository.list.UserRepositoryList;
// import com.dette.entities.User;
// // import com.dette.repository.Repository;

// import com.dette.services.UserService;



// public class UserServiceImpl implements UserService {

//     // private UserRepositoryList userRepository;
//     private Repository<User> userRepository;
    
    

//     // Injection dependance 
//         // Constructeur

//     public UserServiceImpl(Repository<User> userRepository) {
//         this.userRepository = userRepository;
//     }
//     @Override
//     public void createUser(User user) {
//         userRepository.insert(user);
//     }
//     @Override
//     public List<User> findAllUser() {
//         return userRepository.selectAll();
//     }

//     @Override
//     public void activateUser(String login) {
//         userRepository.activateUser(login);
//     }

//     @Override
//     public void desactivateUser(String login) {
//         userRepository.desactivateUser(login);
//     }

//     @Override
//     public User findByLogin(String login) {
//         return userRepository.selectByLogin(login);
//     }
// }
