package com.dette.repository.bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.dette.core.Bd.DatabaseImpl;
import com.dette.entities.Article;
import com.dette.repository.ArticleRepository;

public class ArticleRepositoryBD extends DatabaseImpl implements ArticleRepository {

    @Override
    public void insert(Article article) {
        openConnection();
        String sql = "INSERT INTO article (nom, prix, qte_stock) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, article.getNom());
            stmt.setDouble(2, article.getPrix());
            stmt.setInt(3, article.getQteStock());
            stmt.executeUpdate();
            System.out.println("Article ajouté avec succès!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public List<Article> selectAll() {
        openConnection();
        String sql = "SELECT * FROM article";
        List<Article> articles = new ArrayList<>();
        try {
            ResultSet rs = this.executeSelect(sql);
            while (rs.next()) {
                Article article = new Article();
                article.setNom(rs.getString("nom"));
                article.setPrix(rs.getDouble("prix"));
                article.setQteStock(rs.getInt("qte_stock"));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return articles;
    }

    @Override
    public void updateStock(String nom, int qte_stock) {
        String sql = "UPDATE article SET qte_stock = ? WHERE nom = ?";

        try {

            openConnection();

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, qte_stock); 
                stmt.setString(2, nom);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Quantité en stock mise à jour avec succès !");
                } else {
                    System.out.println("Aucun article trouvé avec ce nom.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la mise à jour de la quantité en stock.");
            }
        } finally {

            closeConnection();
        }
    }

}
