package com.dette.repository.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.dette.core.Bd.DatabaseImpl;
import com.dette.entities.Client;
import com.dette.repository.ClientRepository;

public class ClientRepositoryBD extends DatabaseImpl implements ClientRepository {
    
    @Override
    public void insert(Client client) {
        openConnection();
        String query = "INSERT INTO client (surname, telephone, adresse) VALUES (?, ?, ?)";
    
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getSurname());
            stmt.setString(2, client.getTelephone());
            stmt.setString(3, client.getAdresse());
    
            int rowsAffected = stmt.executeUpdate();
    
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1)); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(); 
        }
    }
    

    @Override
    public List<Client> selectAll() {
        openConnection();
        String sql = "SELECT * FROM client";
        List<Client> clients = new ArrayList<>();
        try (ResultSet rs = this.executeSelect(sql)) {
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setSurname(rs.getString("surname"));
                client.setTelephone(rs.getString("telephone"));
                client.setAdresse(rs.getString("adresse"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(); 
        }
        return clients;
    }

    @Override
    public Client selectBySurname(String surname) {
        openConnection();
        String sql = "SELECT * FROM client WHERE surname = ?";
        Client client = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, surname);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setSurname(rs.getString("surname"));
                client.setTelephone(rs.getString("telephone"));
                client.setAdresse(rs.getString("adresse"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(); 
        }
        return client;
    }

    @Override
    public Client selectByTelephone(String telephone) {
        openConnection();
        String sql = "SELECT * FROM client WHERE telephone = ?";
        Client client = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, telephone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setSurname(rs.getString("surname"));
                client.setTelephone(rs.getString("telephone"));
                client.setAdresse(rs.getString("adresse"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(); 
        }
        return client;
    }


    public Client findClientById(int id) {
        String sql = "SELECT * FROM client WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client(
                        rs.getInt("id"), 
                        rs.getString("surname"),
                        rs.getString("telephone"),
                        rs.getString("adresse")
                    );
                    return client;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    @Override
    public Client selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }


    

    

    
}
