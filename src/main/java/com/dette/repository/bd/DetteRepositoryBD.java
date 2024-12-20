package com.dette.repository.bd;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.dette.core.Bd.DatabaseImpl;
import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Dette;
import com.dette.repository.DetteRepository;

public class DetteRepositoryBD extends DatabaseImpl implements DetteRepository {

    @Override
    public void insert(Dette dette) {
        if (dette.getClient() == null) {
            System.out.println("Aucun client associé à cette dette.");
            return; 
        }

        openConnection();
        String query = "INSERT INTO dette (date, montant, montantverser, statut, client_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(dette.getDate()));
            stmt.setDouble(2, dette.getMontant()); 
            stmt.setDouble(3, dette.getMontantVerser());
            stmt.setString(4, dette.getStatut().toString());

            if (dette.getClient() != null) {
                stmt.setInt(5, dette.getClient().getId());
            }

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    dette.setId(generatedKeys.getInt(1)); 
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de la dette : " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public List<Dette> selectAll() {
        String sql = "SELECT * FROM dette";
        List<Dette> dettes = new ArrayList<>();

        try (ResultSet rs = this.executeSelect(sql)) {
            while (rs.next()) {
                Dette dette = new Dette(0, null, 0);
                dette.setId(rs.getInt("id"));
                LocalDate date = rs.getDate("date").toLocalDate();
                dette.setDate(date);
                dette.setMontant(rs.getDouble("montant"));
                dette.setMontantVerser(rs.getDouble("montantVerser"));
                dette.setMontantRestant(rs.getDouble("montantRestant"));
                dette.setStatut(rs.getString("statut"));
                int clientId = rs.getInt("client_id");
                String clientSql = "SELECT * FROM client WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(clientSql)) {
                    stmt.setInt(1, clientId);
                    try (ResultSet clientRs = stmt.executeQuery()) {
                        if (clientRs.next()) {
                            Client client = new Client();
                            client.setId(clientRs.getInt("id"));
                            client.setSurname(clientRs.getString("surname"));
                            client.setTelephone(clientRs.getString("telephone"));
                            client.setAdresse(clientRs.getString("adresse"));
                            dette.setClient(client);
                        }
                    }
                }

                dettes.add(dette);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dettes;
    }

    public Dette findById(int id) {
        openConnection();
        String query = "SELECT * FROM dette WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Dette dette = new Dette(
                            rs.getInt("id"),
                            rs.getDate("date").toLocalDate(),
                            rs.getDouble("montant"));
                    dette.setMontantVerser(rs.getDouble("montantVerser"));
                    dette.setMontantRestant(rs.getDouble("montantRestant"));
                    dette.setStatut(rs.getString("statut"));
                    return dette;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return null;
    }

    @Override
    public void enregistrerPaiement(Dette dette, double montantPaiement) {
        if (montantPaiement <= 0) {
            System.out.println("Le montant du paiement doit être supérieur à zéro.");
            return;
        }
        if (montantPaiement > dette.getMontantRestant()) {
            System.out.println("Le montant du paiement ne peut pas dépasser le montant restant de la dette.");
            return;
        }
        String updateDetteQuery = "UPDATE dette SET montantrestant = montantrestant - ?, montantverser = montantverser + ? WHERE id = ?";
        String insertPaiementQuery = "INSERT INTO paiement (datepaiement, montantpaiement, dette_id) VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            System.out.println("Mise à jour de la dette avec l'ID " + dette.getId() + "...");
            try (PreparedStatement updateStmt = connection.prepareStatement(updateDetteQuery)) {
                updateStmt.setDouble(1, montantPaiement);
                updateStmt.setDouble(2, montantPaiement); 
                updateStmt.setInt(3, dette.getId());
                int rowsAffected = updateStmt.executeUpdate();
                System.out.println("Lignes affectées par la mise à jour de la dette : " + rowsAffected);

                if (rowsAffected > 0) {
                    System.out.println("Insertion du paiement...");
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertPaiementQuery)) {
                        insertStmt.setDate(1, Date.valueOf(LocalDate.now()));
                        insertStmt.setDouble(2, montantPaiement);
                        insertStmt.setInt(3, dette.getId());
                        int insertRows = insertStmt.executeUpdate();
                        System.out.println("Lignes affectées par l'insertion du paiement : " + insertRows);
                        if (insertRows > 0) {
                            connection.commit();
                            System.out.println("Paiement enregistré avec succès !");
                        } else {
                            connection.rollback();
                            System.out.println("Échec de l'insertion du paiement, transaction annulée.");
                        }
                    }
                } else {
                    System.out.println("Échec de la mise à jour de la dette.");
                    connection.rollback();
                }
            }
            if (dette.getMontantRestant() <= 0) {
                String updateStatusQuery = "UPDATE dette SET statut = 'SOLDEE' WHERE id = ?";
                System.out.println("Mise à jour du statut de la dette...");
                try (PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusQuery)) {
                    updateStatusStmt.setInt(1, dette.getId());
                    int statusRowsAffected = updateStatusStmt.executeUpdate();
                    System.out.println(
                            "Lignes affectées par la mise à jour du statut de la dette : " + statusRowsAffected);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur de transaction : " + e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Dette> listerDettesNonSoldees() {
        String sql = "SELECT * FROM dette WHERE statut != 'SOLDEE'";
        List<Dette> dettes = new ArrayList<>();
        try (ResultSet rs = this.executeSelect(sql)) {
            while (rs.next()) {
                Dette dette = new Dette(
                        rs.getInt("id"),
                        rs.getDate("date_creation").toLocalDate(),
                        rs.getDouble("montant"));
                dette.setMontantRestant(rs.getDouble("montant_rester"));
                dette.setStatut(rs.getString("statut"));
                dette.setClientId(rs.getInt("client_id"));
                dettes.add(dette);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dettes;
    }

    public void associerClientADette(Dette dette, String telephoneClient) {
        Client client = getClientByTelephone(telephoneClient); 

        if (client != null) {
            System.out.println("Client trouvé : " + client.getSurname() + " " + client.getAdresse());
            dette.setClient(client);
        } else {
            System.out.println("Client non trouvé avec ce numéro de téléphone : " + telephoneClient);
        }
    }

    public Client getClientByTelephone(String telephone) {
        System.out.println("Recherche du client avec le téléphone : " + telephone);
        String query = "SELECT * FROM client WHERE telephone = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, telephone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setSurname(rs.getString("surname"));
                client.setTelephone(rs.getString("telephone"));
                client.setAdresse(rs.getString("adresse"));
                System.out.println("Client trouvé : " + client.getSurname() + " " + client.getAdresse());
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Aucun client trouvé pour ce téléphone.");
        return null; 
    }

    @Override
    public List<Dette> listerDettesEnCours() {
        throw new UnsupportedOperationException("Unimplemented method 'listerDettesEnCours'");
    }

    @Override
    public void insert(Dette dette, String telephone) {
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void validerRefuserDette(int detteId, boolean valider) {
        String statut = valider ? "VALIDEE" : "REFUSEE"; 
        String query = "UPDATE dette SET statut = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, statut);
            stmt.setInt(2, detteId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("La dette " + detteId + " a été " + statut.toLowerCase() + " avec succès.");
            } else {
                System.out.println("Aucune dette trouvée avec l'ID " + detteId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la validation ou du refus de la dette.");
        }
    }

    @Override
    public void listerDettesNonSoldees(int clientId) {
        String query = "SELECT id, date, montant, montantRestant, statut " +
                "FROM dette " +
                "WHERE client_id = ? AND montantRestant > 0";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Dettes non soldées pour le client ID : " + clientId);
                boolean found = false;

                while (rs.next()) {
                    int id = rs.getInt("id");
                    Date date = rs.getDate("date");
                    double montant = rs.getDouble("montant");
                    double montantRestant = rs.getDouble("montantRestant");
                    String statut = rs.getString("statut");

                    System.out.println("Dette ID: " + id + ", Date: " + date + ", Montant: " + montant +
                            ", Restant: " + montantRestant + ", Statut: " + statut);
                    found = true;
                }

                if (!found) {
                    System.out.println("Aucune dette non soldée trouvée.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des dettes non soldées.");
        }
    }

    public void afficherPaiementsDette(int detteId) {
        String query = "SELECT id, datePaiement, montantPaiement " +
                "FROM paiement " +
                "WHERE dette_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, detteId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Paiements associés à la dette ID : " + detteId);
                boolean found = false;

                while (rs.next()) {
                    int id = rs.getInt("id");
                    Date date = rs.getDate("datePaiement");
                    double montantPaiement = rs.getDouble("montantPaiement");

                    System.out.println("Paiement ID: " + id + ", Date: " + date + ", Montant: " + montantPaiement);
                    found = true;
                }

                if (!found) {
                    System.out.println("Aucun paiement trouvé pour cette dette.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des paiements.");
        }
    }

    public void faireDemandeDette(int clientId, double montant) {
        String query = "INSERT INTO demande_dette (client_id, montant, statut, dateDemande) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            stmt.setDouble(2, montant);
            stmt.setString(3, "EN COURS");
            stmt.setDate(4, Date.valueOf(LocalDate.now()));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Demande de dette créée avec succès.");
            } else {
                System.out.println("Échec de la création de la demande de dette.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la création de la demande de dette.");
        }
    }

    public void listerDemandesDette(int clientId, String statut) {
        String query = "SELECT id, montant, statut, dateDemande " +
                "FROM demande_dette " +
                "WHERE client_id = ?";

        if (statut != null) {
            query += " AND statut = ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, clientId);

            if (statut != null) {
                stmt.setString(2, statut);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Demandes de dette pour le client ID : " + clientId);
                boolean found = false;

                while (rs.next()) {
                    int id = rs.getInt("id");
                    double montant = rs.getDouble("montant");
                    String currentStatut = rs.getString("statut");
                    Date dateDemande = rs.getDate("dateDemande");

                    System.out.println("Demande ID: " + id + ", Montant: " + montant + ", Statut: " + currentStatut +
                            ", Date: " + dateDemande);
                    found = true;
                }

                if (!found) {
                    System.out.println("Aucune demande de dette trouvée.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des demandes de dette.");
        }
    }

    @Override
    public void afficherArticlesDette(int detteId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afficherArticlesDette'");
    }

    

}
