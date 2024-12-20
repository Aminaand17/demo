package com.dette.repository.bd;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dette.core.Bd.DatabaseImpl;
import com.dette.entities.Paiement;
import com.dette.repository.list.PaiementRepository;

public class PaiementRepositoryBD extends DatabaseImpl implements PaiementRepository {


    @Override
    public void insert(Paiement paiement, int detteId) {
        String query = "INSERT INTO paiement (datePaiement, montantPaiement, dette_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(paiement.getDatePaiement()));
            stmt.setDouble(2, paiement.getMontantPaiement());
            stmt.setInt(3, detteId);
            stmt.executeUpdate();
            System.out.println("Paiement ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Paiement> findByDetteId(int detteId) {
        List<Paiement> paiements = new ArrayList<>();
        String query = "SELECT * FROM paiement WHERE dette_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, detteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Paiement paiement = new Paiement(
                        rs.getDate("datePaiement").toLocalDate(),
                        rs.getDouble("montantPaiement")
                    );
                    paiements.add(paiement);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    @Override
    public List<Paiement> findAll() {
        List<Paiement> paiements = new ArrayList<>();
        String query = "SELECT * FROM paiement";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Paiement paiement = new Paiement(
                    rs.getDate("datePaiement").toLocalDate(),
                    rs.getDouble("montantPaiement")
                );
                paiements.add(paiement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }
}

