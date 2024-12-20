package com.dette.repository.bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.dette.core.Bd.DatabaseImpl;
import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.User;

public class AdminRepositoryBD extends DatabaseImpl {


    public List<Client> listerClients() {
        openConnection(); 
        String sql = "SELECT c.id AS client_id, c.surname, c.telephone, c.adresse, " +
                "u.id AS user_id, u.login, u.role, u.actif " +
                "FROM client c " +
                "LEFT JOIN utilisateur u ON c.id = u.client_id"; 
        List<Client> clients = new ArrayList<>();
        try (ResultSet rs = this.executeSelect(sql)) {
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setSurname(rs.getString("surname"));
                client.setTelephone(rs.getString("telephone"));
                client.setAdresse(rs.getString("adresse"));

                int userId = rs.getInt("user_id");
                if (userId > 0) {
                    User user = new User();
                    user.setId(userId);
                    user.setLogin(rs.getString("login"));
                    user.setRole(rs.getString("role"));
                    user.setActive(rs.getBoolean("actif"));
                    client.setUser(user);
                } else {
                    client.setUser(null);
                }

                clients.add(client); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(); 
        }
        return clients;
    }


    public void creerCompteUtilisateur(String login, String password, String role, int clientId) {
        String query = "INSERT INTO user (login, password, role, client_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.setInt(4, clientId);
            stmt.executeUpdate();
            System.out.println("Compte utilisateur créé avec succès.");


            mettreAJourClientAvecCompte(clientId, login, role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mettreAJourClientAvecCompte(int clientId, String login, String role) {
        openConnection();
        String sql = "SELECT c.*, u.id AS user_id, u.login, u.role, u.actif " +
                "FROM client c " +
                "JOIN user u ON c.id = u.client_id " +
                "WHERE c.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client();
                    client.setId(rs.getInt("id"));
                    client.setSurname(rs.getString("surname"));
                    client.setTelephone(rs.getString("telephone"));
                    client.setAdresse(rs.getString("adresse"));
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setLogin(rs.getString("login"));
                    user.setRole(rs.getString("role"));
                    user.setActive(rs.getBoolean("actif"));
                    client.setUser(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public int getRoleIdByName(String roleName) {
        String sql = "SELECT id FROM role WHERE nom = ?";
        int roleId = -1;
        openConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                roleId = rs.getInt("id");
            } else {
                System.out.println("Rôle introuvable : " + roleName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return roleId;
    }


    public void creerCompteUtilisateurAvecRole(String nom, String prenom, String login, String password, String role) {
        int roleId = getRoleIdByName(role);
        if (roleId == -1) {
            System.out.println("Le rôle spécifié est invalide : " + role);
            return;
        }

        String query = "INSERT INTO utilisateur (nom, prenom, login, password, role_id) VALUES (?, ?, ?, ?, ?)";
        openConnection();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, login);
            stmt.setString(4, password);
            stmt.setInt(5, roleId); 
            stmt.executeUpdate();
            System.out.println("Compte utilisateur créé avec succès pour : " + nom + " " + prenom);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }


    public void activerCompteUtilisateur(int userId) {
        String query = "UPDATE user SET actif = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            System.out.println("Compte activé avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void desactiverCompteUtilisateur(int userId) {
        String query = "UPDATE user SET actif = FALSE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            System.out.println("Compte désactivé avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> listerComptesUtilisateursActifs() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE actif = TRUE";
        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"),
                        rs.getString("password"), rs.getInt("roleId"));
                user.setActive(rs.getBoolean("actif"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public List<User> listerUtilisateursParRole(String role) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE role = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"),
                            rs.getString("password"), rs.getInt("roleId"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public void creerArticle(String nom, String reference, int qteStock, double prix) {
        String query = "INSERT INTO article (nom, reference, qteStock, prix) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, reference);
            stmt.setInt(3, qteStock);
            stmt.setDouble(4, prix);
            stmt.executeUpdate();
            System.out.println("Article créé avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Article> listerArticles() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article";
        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Article article = new Article();
                article.setNom(rs.getString("nom"));
                article.setReference(rs.getString("reference"));
                article.setQteStock(rs.getInt("qteStock"));
                article.setPrix(rs.getDouble("prix"));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }


    public List<Article> filtrerArticlesDisponibles() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article WHERE qteStock > 0";
        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Article article = new Article();
                article.setNom(rs.getString("nom"));
                article.setReference(rs.getString("reference"));
                article.setQteStock(rs.getInt("qteStock"));
                article.setPrix(rs.getDouble("prix"));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    public void mettreAJourQuantiteArticle(int articleId, int nouvelleQte) {
        String query = "UPDATE article SET qteStock = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, nouvelleQte);
            stmt.setInt(2, articleId);
            stmt.executeUpdate();
            System.out.println("Quantité en stock mise à jour.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void archiverDettesSoldees() {
        String query = "UPDATE dette SET statut = 'ARCHIVEE' WHERE statut = 'SOLDEE'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.executeUpdate();
            System.out.println("Dettes soldées archivées.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
