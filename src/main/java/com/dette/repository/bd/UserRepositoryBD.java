package com.dette.repository.bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.dette.core.Bd.DatabaseImpl;
import com.dette.entities.Client;
import com.dette.entities.User;
import com.dette.repository.UserRepository;
import com.dette.repository.ClientRepository;

public class UserRepositoryBD extends DatabaseImpl implements UserRepository {

    private ClientRepository clientRepository;

    @Override
    public void insert(User user) {
        openConnection();
        int roleId = getRoleIdByName(user.getRole()); 
        if (roleId == -1) {
            System.out.println("Erreur : Le rôle spécifié n'est pas valide (seulement Admin ou Boutiquier).");
            closeConnection();
            return;
        }

        String sql = "INSERT INTO utilisateur (nom, prenom, login, password, role_id, active) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setString(3, user.getLogin());
            stmt.setString(4, user.getPassword());
            stmt.setInt(5, roleId); 
            stmt.setBoolean(6, true);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Utilisateur ajouté avec succès!");
            } else {
                System.out.println("Aucun utilisateur n'a été ajouté.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public List<User> selectAll() {
        openConnection();
        String sql = "SELECT u.id, u.nom, u.prenom, u.login, u.password, r.nom AS role_name, u.role_id, u.active " +
                "FROM utilisateur u " +
                "JOIN role r ON u.role_id = r.id";
        List<User> users = new ArrayList<>();
        try {
            ResultSet rs = this.executeSelect(sql);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getInt("role_id"));
                user.setRole(rs.getString("role_name"));
                user.setActive(rs.getBoolean("active"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return users;
    }

    @Override
    public void activateUser(String login) {
        openConnection();
        String sql = "UPDATE utilisateur SET active = true WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.executeUpdate();
            System.out.println("Utilisateur activé avec succès!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void desactivateUser(String login) {
        openConnection();
        String sql = "UPDATE utilisateur SET active = false WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.executeUpdate();
            System.out.println("Utilisateur désactivé avec succès!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public List<User> selectByRole(String roleName) {
        openConnection();
        String sql = "SELECT u.id, u.nom, u.prenom, u.login, u.password, r.nom AS role_name, u.role_id, u.active " +
                "FROM utilisateur u " +
                "JOIN role r ON u.role_id = r.id " +
                "WHERE r.nom = ?";
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getInt("role_id"));
                user.setRole(rs.getString("role_name"));
                user.setActive(rs.getBoolean("active"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return users;
    }

    @Override
    public User selectByLogin(String login) {
        openConnection();
        String sql = "SELECT * FROM utilisateur WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role_id"));
                user.setActive(rs.getBoolean("active"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return null; 
    }

    @Override
    public User getUserByLogin(String login) {
        String sql = "SELECT u.id, u.nom, u.prenom, u.login, u.password, u.role_id, r.nom AS role_name " +
                "FROM utilisateur u " +
                "JOIN role r ON u.role_id = r.id " +
                "WHERE u.login = ?";
        User user = null;
        openConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getInt("role_id"));
                user.setRole(rs.getString("role_name"));
            } else {
                System.out.println("Aucun utilisateur trouvé avec ce login.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return user;
    }

    @Override
    public void createAccountForClientByTelephone(String telephone, String login, String password) {
        openConnection();
        Client client = clientRepository.selectByTelephone(telephone);
        if (client == null) {
            System.out.println("Erreur : Aucun client trouvé avec ce numéro de téléphone.");
            closeConnection();
            return;
        }

        String roleName = "Client"; 
        int roleId = getRoleIdByName(roleName);

        if (roleId == -1) {
            System.out.println("Erreur : Le rôle 'Client' n'existe pas dans la base de données.");
            closeConnection();
            return;
        }
        String sql = "INSERT INTO utilisateur (nom, prenom, login, password, role_id, active) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getSurname()); 
            stmt.setString(2, "");
            stmt.setString(3, login); 
            stmt.setString(4, password); 
            stmt.setInt(5, roleId); 
            stmt.setBoolean(6, true); 

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Compte utilisateur pour le client créé avec succès!");
            } else {
                System.out.println("Aucun compte utilisateur n'a été créé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du compte utilisateur : " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private int getRoleIdByName(String roleName) {
        String sql = "SELECT id FROM role WHERE nom = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID du rôle : " + e.getMessage());
            e.printStackTrace();
        }
        return -1; 
    }

    @Override
    public List<User> selectActiveUsers() {
        List<User> users = new ArrayList<>();
        openConnection();
        String sql = "SELECT u.nom, u.prenom, u.login, u.password, r.role_name, u.active " +
                     "FROM utilisateur u " +
                     "JOIN role r ON u.role_id = r.id " +
                     "WHERE u.active = true"; 
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role_name")); 
                user.setActive(rs.getBoolean("active"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return users;
    }
    
@Override
public List<User> selectUsersByRole(String role) {
    List<User> users = new ArrayList<>();
    openConnection();
    String sql = "SELECT * FROM utilisateur WHERE role_id = ?"; 
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, role);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role_id"));
            user.setActive(rs.getBoolean("active"));
            users.add(user);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        closeConnection();
    }
    return users;
}


}
