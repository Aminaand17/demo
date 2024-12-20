package com.dette.core.Bd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {


    private static final String URL = "jdbc:mysql://localhost:3306/javamaven";
    private static final String USER = "root";  
    private static final String PASSWORD = "nouveau_mot_de_passe"; 

    public static Connection getConnection() {
        Connection connection = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie !");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur de chargement du driver MySQL");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données");
            e.printStackTrace();
        }

        return connection;
    }

}
